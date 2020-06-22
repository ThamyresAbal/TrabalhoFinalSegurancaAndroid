package com.app.at_seguranca

import android.R.attr.bitmap
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.app.at_seguranca.adapter.ArquivoAdapter
import kotlinx.android.synthetic.main.activity_notas.*
import java.io.*


class NotasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arquivoAdapter: ArquivoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var fotoBitmap  : Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)


        btnCriar.setOnClickListener(){
            gravarArquivoTxt(txtTitulo.text.toString(), textDataAtual.text.toString(), textAnotacao.text.toString())

            gravarArquivoFig(txtTitulo.text.toString(), textDataAtual.text.toString(), fotoBitmap)
            startActivity(Intent(this, HomeActivity::class.java))
        }

        imageView.setOnClickListener{
            onCameraClick(this)
        }

    }

    private val REQUEST_CAPTURE_IMAGE = 100

    fun onCameraClick(view: NotasActivity){
        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )
        if (pictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(
                pictureIntent,
                REQUEST_CAPTURE_IMAGE
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
            resultCode == Activity.RESULT_OK
        ) {
            if (data != null && data.extras != null) {
                val fotoTiradaBitmap = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(fotoTiradaBitmap)
                fotoBitmap = fotoTiradaBitmap
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun getEncFile(nome: String): EncryptedFile{
        val masterkeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val file: java.io.File = java.io.File(applicationContext.filesDir, nome)
        return EncryptedFile.Builder(
            file,
            applicationContext,
            masterkeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

    }

    fun gravarArquivoTxt(titulo : String, data : String, anotacao: String){

        val encryptedOut: FileOutputStream =
            getEncFile(titulo+data+".txt").openFileOutput()

        val pw = PrintWriter(encryptedOut)
        pw.println("titulo: $titulo , data : $data , $anotacao")
        pw.flush()
        encryptedOut.close()
    }

    fun gravarArquivoFig(titulo : String, data : String, fotoA: Bitmap){

        val encryptedOut: FileOutputStream = getEncFile(titulo+data+".fig").openFileOutput()

        var baos = ByteArrayOutputStream()
        fotoA.compress(Bitmap.CompressFormat.PNG, 90, baos)
        var imagemByteArray = baos.toByteArray()

        encryptedOut.write(imagemByteArray)
        encryptedOut.close()

    }

    fun pegarArquivoTxt(titulo : String, data : String) : String{

        val encryptedIn: FileInputStream =
            getEncFile(titulo+data+".txt").openFileInput()

        var textoString : String = ""
        val br = BufferedReader(InputStreamReader(encryptedIn))
        br.lines().forEach{
            textoString = it.toString()
        }
        encryptedIn.close()

        return textoString

    }

    fun pegarArquivoFig(titulo : String, data : String) : Bitmap{

        val encryptedIn: FileInputStream =
            getEncFile(titulo+data+".fig").openFileInput()

        var bmp = BitmapFactory.decodeStream(encryptedIn)

        return bmp
    }


}