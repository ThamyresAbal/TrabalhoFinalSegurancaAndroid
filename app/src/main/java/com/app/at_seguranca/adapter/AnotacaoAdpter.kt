package com.app.at_seguranca.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.at_seguranca.R
import com.app.at_seguranca.model.Anotacao
import kotlinx.android.synthetic.main.layout_lista.view.*


class AnotacaoAdpter(
    private val listaAnotacao: List<Anotacao>,
    val mostraAnotacao:(Anotacao, Context,view:View)-> Unit

) : RecyclerView.Adapter
<AnotacaoAdpter.AnotacaoViewHolder>() {

    class AnotacaoViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val textNomeAnotacao = itemView.txtNomeAnotacao
        val data = itemView.txtData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_lista,
                parent,
                false
            )

        val anotacaoViewHolder = AnotacaoViewHolder(view)

        anotacaoViewHolder.itemView.setOnClickListener {
            val anime = listaAnotacao[anotacaoViewHolder.adapterPosition]
            mostraAnotacao(anime, parent.context,view)
        }

        return anotacaoViewHolder
    }
    override fun getItemCount(): Int = listaAnotacao.size

    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {
        val listaAnotacao = listaAnotacao[position]
        holder.textNomeAnotacao.text = listaAnotacao.titulo
        holder.data.text = listaAnotacao.data

    }
}