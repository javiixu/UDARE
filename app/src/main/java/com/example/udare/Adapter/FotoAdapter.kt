package com.example.udare.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R

class FotoAdapter (private val fotos: List<Int>) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {

    class FotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.foto_viewer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_foto, parent, false)
        return FotoHolder(view)
    }

    override fun onBindViewHolder(holder: FotoHolder, position: Int) {
        //val fotoResId = fotos[position]
        val standardSize = 1200 // Tamaño estándar en píxeles

        val options = RequestOptions()
            .override(standardSize, standardSize)
            .centerCrop()

        Glide.with(holder.imageView)
            .load("https://testudare.s3.eu-west-3.amazonaws.com/169808470849611c4dcddeb988162d976e93ae5512264.jpg")
            .apply(options)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return fotos.size
    }

}