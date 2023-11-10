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
import com.example.udare.presentation.ComentariosActivity
import com.example.udare.presentation.HacerFotoActivity


class FotoAdapter (private val fotos: List<String>, private val context: Context) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {

    class FotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.foto_viewer)
        val textViewClick: TextView = itemView.findViewById(R.id.comentarios)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.udare.R.layout.item_foto, parent, false)
        return FotoHolder(view)
    }

    override fun onBindViewHolder(holder: FotoHolder, position: Int) {
        val fotoResId = fotos[position]
        val standardSize = 1200 // Tamaño estándar en píxeles

        val options = RequestOptions()
            .override(standardSize, standardSize)
            .centerCrop()

        Glide.with(holder.imageView)
            .load(fotoResId)
            .apply(options)
            .into(holder.imageView)

        holder.textViewClick.setOnClickListener {
            // Crear un Intent para iniciar la nueva actividad (HacerFotoActivity)
            val intent = Intent(context, ComentariosActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return fotos.size
    }

}