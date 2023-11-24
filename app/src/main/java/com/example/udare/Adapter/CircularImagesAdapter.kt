package com.example.udare.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R
import com.example.udare.data.model.Reaction

class CircularImagesAdapter(private val Lista: MutableList<Reaction>) :RecyclerView.Adapter<CircularImagesAdapter.CircularImagesHolder>() {
    class CircularImagesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.circularImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularImagesHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_circular_image, parent, false)
        return CircularImagesHolder(itemView)
    }

    override fun onBindViewHolder(holder: CircularImagesHolder, position: Int) {
//        val currentItem = Lista[position]
        val reaction : Reaction = Lista[position]
        Log.d("CircularImagesAdapter", "onBindViewHolder: " + reaction)

        Glide.with(holder.imageView.context)
            .load(reaction.reaction)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.imageView)
    }

    override fun getItemCount() = Lista.size
    fun updateList(reactions: MutableList<Reaction>) {
        Lista.clear()
        Lista.addAll(reactions)
        notifyDataSetChanged()
    }
}