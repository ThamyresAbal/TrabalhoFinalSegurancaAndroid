package com.app.at_seguranca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.app.at_seguranca.R
import com.app.at_seguranca.adapter.ArquivoAdapter
import kotlinx.android.synthetic.main.activity_notas.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        viewManager = LinearLayoutManager(this)
        arquivoAdapter = ArquivoAdapter(ArrayList<File>(obterNotas()))
        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = arquivoAdapter
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

    fun clickAdd(view: View) {
        arquivoAdapter.addNota(gravarNota(textAnotacao.text.toString()))
        textAnotacao.setText("")
    }
    fun clickLer(view: View){
        var nomeNota = (view as TextView).text.toString()
        textAnotacao.setText(lerNota(nomeNota))
    }


}