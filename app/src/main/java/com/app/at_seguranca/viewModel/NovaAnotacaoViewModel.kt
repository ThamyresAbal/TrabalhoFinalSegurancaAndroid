package com.app.at_seguranca.viewModel

import android.content.Context
import android.graphics.Bitmap

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.app.at_seguranca.Cripto.Cripto
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.security.KeyFactory.getInstance
import java.security.KeyStore

class NovaAnotacaoViewModel :ViewModel() {

    fun pegarImagem(context: Context){

    }

    fun salvarImagem(titulo : String, data : String, bytes : ByteArray){
 /*       var arquivo = Cripto().criptografia("$titulo($data).fig")
        val encryptedOut: FileOutputStream = arquivo.openFileOutput()
        encryptedOut.write(bytes)
        encryptedOut.close()*/
    }

/*    fun imagemByte() : ByteArray{
        var bitmap = .drawable.toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = stream.toByteArray()
        stream.close()
        return image
    }*/

}