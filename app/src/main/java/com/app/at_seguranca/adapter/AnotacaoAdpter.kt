package com.app.at_seguranca.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.at_seguranca.R
import com.app.at_seguranca.model.Anotacao
import kotlinx.android.synthetic.main.activity_notas.view.*
import kotlinx.android.synthetic.main.layout_lista.view.*


class AnotacaoAdpter(var listaArquivo : MutableList<Anotacao>) : RecyclerView.Adapter<AnotacaoAdpter.AnotacaoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_lista,parent,false)
        return AnotacaoViewHolder(view)
    }

    override fun getItemCount(): Int = listaArquivo.size

    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {
        holder.bind(listaArquivo[position])
    }

    inner class AnotacaoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(anotacao: Anotacao){

            itemView.txtNomeAnotacao.text = anotacao.titulo
            itemView.textoAnotacao.text = anotacao.texto
            itemView.imageView2.setImageBitmap(anotacao.foto)

        }
    }
}