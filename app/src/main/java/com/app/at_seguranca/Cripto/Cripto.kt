package com.app.at_seguranca.Cripto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Cripto {

    val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    fun getSecretKey(): SecretKey? {
        var chave: SecretKey? = null
        if(ks.containsAlias("CriptografiaChave")) {
            val entrada = ks.getEntry("CriptografiaChave", null) as?
                    KeyStore.SecretKeyEntry
            chave = entrada?.secretKey
        } else {
            val builder = KeyGenParameterSpec.Builder("CriptografiaChave",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            val keySpec = builder.setKeySize(256)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
            val kg = KeyGenerator.getInstance("AES", "AndroidKeyStore")
            kg.init(keySpec)
            chave = kg.generateKey()
        }
        return chave
    }

    fun criptografia(original: String): ByteArray {
        var chave = getSecretKey()
        return criptografia(original,chave)
    }

    fun criptografia(original: String, chave: SecretKey?): ByteArray {
        if (chave != null) {
            Cipher.getInstance("AES/CBC/PKCS7Padding").run {
                init(Cipher.ENCRYPT_MODE,chave)
                var valorCripto = doFinal(original.toByteArray())
                var ivCripto = ByteArray(16)
                iv.copyInto(ivCripto,0,0,16)
                return ivCripto + valorCripto
            }
        } else return byteArrayOf()
    }

    fun decipher(cripto: ByteArray): String{
        var chave = getSecretKey()
        return decipher(cripto,chave)
    }

    fun decipher(cripto: ByteArray, chave: SecretKey?): String{
        if (chave != null) {
            Cipher.getInstance("AES/CBC/PKCS7Padding").run {
                var ivCripto = ByteArray(16)
                var valorCripto = ByteArray(cripto.size-16)
                cripto.copyInto(ivCripto,0,0,16)
                cripto.copyInto(valorCripto,0,16,cripto.size)
                val ivParams = IvParameterSpec(ivCripto)
                init(Cipher.DECRYPT_MODE,chave,ivParams)
                return String(doFinal(valorCripto))
            }
        } else return ""
    }

    fun getHash(texto: String): String{
        var md = MessageDigest.getInstance("MD5")
        return Base64.encodeToString(
            md.digest(texto.toByteArray()), Base64.DEFAULT).trimEnd()
    }
}