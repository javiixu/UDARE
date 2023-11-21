package com.example.udare.Adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R
import com.example.udare.presentation.Inicio


class CommentsAdapter(private val textos: List<String>, private val context: Context) :
    RecyclerView.Adapter<CommentsAdapter.TextoHolder>(){

    class TextoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.comentario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false)
        return TextoHolder(view)
    }

    override fun getItemCount(): Int {
        return textos.size
    }

    override fun onBindViewHolder(holder: TextoHolder, position: Int) {
        val texto = textos[position]

        // Configurar el texto en el TextView
        holder.textView.text = texto

    }


}