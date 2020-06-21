package com.app.at_seguranca.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.at_seguranca.R
import java.io.File

class ArquivoAdapter (private val arquivos: MutableList<File>) :
    RecyclerView.Adapter<ArquivoAdapter.MyViewHolder>() {
    class MyViewHolder(val textView: TextView) :
        RecyclerView.ViewHolder(textView)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ArquivoAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_lista, parent, false) as TextView
        return MyViewHolder(textView)
    }
    override fun onBindViewHolder(holder: MyViewHolder,
                                  position: Int) {
        holder.textView.text = arquivos[position].name
    }

    override fun getItemCount() = arquivos.size

    fun addNota(nota: File){
        arquivos.add(nota)
        notifyItemInserted(itemCount)
    }
}
