package com.app.at_seguranca.viewModel

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.at_seguranca.R
import com.app.at_seguranca.adapter.AnotacaoAdpter
import com.app.at_seguranca.model.Anotacao
import kotlinx.android.synthetic.main.dialog_lista.view.*

class ListaViewModel : ViewModel(){
//    lateinit var anotacao :Anotacao
//
//fun recyclerConfig(recyclerView: RecyclerView, context: Context){
//
//    val lista = listOf<Anotacao>()
//    recyclerView.adapter = AnotacaoAdpter(lista, this::anotacao)
//    recyclerView.layoutManager = LinearLayoutManager(context)
//    Log.i("tag","funciona")
//}
//    fun anotacao(anotacao: Anotacao, context: Context,view:View){
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_lista,null)
//        val builder = AlertDialog.Builder(context)
//            .setView(dialogView)
//            .setTitle("Anotação")
//            .show()
//
//        dialogView.buttonVoltar.setOnClickListener {
//            builder.dismiss()
//        }
//
//    }

}