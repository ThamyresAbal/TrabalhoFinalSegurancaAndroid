package com.app.at_seguranca

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.app.at_seguranca.R
import com.app.at_seguranca.adapter.ArquivoAdapter
import com.app.at_seguranca.viewModel.NovaAnotacaoViewModel
import kotlinx.android.synthetic.main.activity_notas.*
import kotlinx.android.synthetic.main.activity_notas.getImageView
import kotlinx.android.synthetic.main.activity_notas.imageView
import kotlinx.android.synthetic.main.fragment_nova_anotacao.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

class NotasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arquivoAdapter: ArquivoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var novaAnotacaoViewModel: NovaAnotacaoViewModel
    private var TAKE_PICTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        novaAnotacaoViewModel = ViewModelProviders.of(this).get(NovaAnotacaoViewModel::class.java)
        viewManager = LinearLayoutManager(this)
        arquivoAdapter = ArquivoAdapter(ArrayList<File>(obterNotas()))
        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = arquivoAdapter
        }
        getImageView.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(this.packageManager)?.also {
                    startActivityForResult(takePictureIntent, TAKE_PICTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun obterNotas():List<File>{
        var diretorio = obterDiretorio("notas",true)
        return diretorio.listFiles().filter {
                t -> t.name.endsWith(".note")
        }
    }

    fun obterDiretorio(diretorio: String, criar: Boolean):File{
        var dirArq = getExternalFilesDir(null)!!.path + "/" + diretorio
        var dirFile = File(dirArq)
        if(!dirFile.exists()&&(!criar||!dirFile.mkdirs()))
            throw Exception("Diretório indisponível")
        return dirFile
    }

    private fun gravarNota(texto: String): File{
        var diretorio = obterDiretorio("notas",false)
        val masterKeyAlias: String =
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        var nota = File(diretorio.path+"/"+ Date().time+".note")
        var encryptedOut = EncryptedFile.Builder(
            nota, applicationContext, masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build().openFileOutput()
        val pw = PrintWriter(encryptedOut)
        pw.println(texto)
        pw.flush()
        encryptedOut.close()
        return nota
    }

    fun lerNota(nomeNota: String): String{
        var diretorio = obterDiretorio("notas",false)
        val masterKeyAlias: String =
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        var nota = File(diretorio.path+"/"+nomeNota)
        var encryptedIn = EncryptedFile.Builder(
            nota, applicationContext, masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build().openFileInput()
        val br = BufferedReader(InputStreamReader(encryptedIn))
        val sb = StringBuffer()
        br.lines().forEach{ t -> sb.append(t+"\n") }
        encryptedIn.close()
        return sb.toString()
    }

    /*fun clickAdd() {
        arquivoAdapter.addNota(gravarNota(textAnotacao.text.toString()))
        textAnotacao.setText("")
    }
    fun clickLer(){
        var nomeNota = (view as TextView).text.toString()
        textAnotacao.setText(lerNota(nomeNota))
    }
*/

}