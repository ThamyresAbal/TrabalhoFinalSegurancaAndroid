package com.app.at_seguranca.Cripto

import android.util.Base64

class CriptoString {
    companion object{
        @JvmStatic
        val criptoGrafador = Cripto()
    }

    private var cripto: ByteArray? = null

    fun getCriptoBase64(): String?{
        return Base64.encodeToString(cripto,Base64.DEFAULT)
    }
    fun setCriptoBase64(value: String?){
        cripto = Base64.decode(value,Base64.DEFAULT)
    }
    fun getClearText(): String?{
        return criptoGrafador.decipher(cripto!!)
    }
    fun setClearText(value: String?){
        cripto = criptoGrafador.criptografia(value!!)
    }
}
