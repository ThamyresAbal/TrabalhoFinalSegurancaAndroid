package com.app.at_seguranca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.app.at_seguranca.viewModel.ListaViewModel
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null)
        {
            var intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
    }
}