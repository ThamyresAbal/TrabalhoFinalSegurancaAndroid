package com.app.at_seguranca.viewModel

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.app.at_seguranca.HomeActivity
import com.app.at_seguranca.MainActivity
import com.app.at_seguranca.R
import com.app.at_seguranca.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_lista.view.*
import kotlinx.android.synthetic.main.fragment_cadastro.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class UsuarioViewModel : ViewModel(){
    var usuario: Usuario? = null
    val firebaseAuth = FirebaseAuth.getInstance()
    private var uniqueID: String? = null
    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

    fun verificarNulo(
        view: View, context: Context
    ): Boolean {
        if (view.txtSenha.text.isNullOrBlank() || view.txtEmail.text.isNullOrBlank()) {
            return false
        } else {
             usuario = Usuario(
                email = view.txtEmail.text.toString(),
                senha = view.txtSenha.text.toString()
            )
            return true
        }
    }
    fun criarAuth(email:String, senha: String, context: Context){
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                if(it != null){
                    Toast.makeText(context, "Cadastro realizado com sucesso",
                        Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("autentificacao", "cadastrado")
                }
            }
            .addOnFailureListener {
                if(it.message == "The email address is already in use by another account"){
                    Toast.makeText(context, "Email já cadastrado!", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Erro", "Erro no cadastro")
                }
            }
    }
    fun loginFirestore(context: Context, boxEmail: String, boxSenha: String){
        firebaseAuth.signInWithEmailAndPassword(boxEmail, boxSenha)
            .addOnSuccessListener {
                Toast.makeText(context, "Bem vindo ${it.user!!.email}", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            .addOnFailureListener {
                if(it.message == "The email address is baldy formatted"){
                    Toast.makeText(context, "Por favor insira um email com formato válido", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Email ou senha inválidos!", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun logout(context: Context) {
        val myDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_lista, null)
        val myBuilder = AlertDialog.Builder(context)
            .setView(myDialogView)
            .setTitle("Tem certeza que deseja sair?")
        val myAlertDialog = myBuilder.show()
        myDialogView.buttonVoltar.setOnClickListener {
            myAlertDialog.dismiss()
        }
        myDialogView.buttonSair.setOnClickListener {
            firebaseAuth.signOut()
            context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
    fun identificarUsuario(textNome :View){
        textNome.textEmailUsuario.text = firebaseAuth.currentUser!!.email
    }
    @Synchronized
    fun preferenciaUsuario(context: Context): String?{
        if (uniqueID == null) {
            val sharedPrefs: SharedPreferences =
                context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE)
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueID)
                editor.commit()
            }
        }
        return uniqueID
    }
}