package com.example.udare.Adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R
import com.example.udare.data.model.CommentData
import com.example.udare.presentation.Inicio


class CommentsAdapter(private val Lista: List<CommentData>, private val context: Context) :
    RecyclerView.Adapter<CommentsAdapter.TextoHolder>(){

    class TextoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentView: TextView = itemView.findViewById(R.id.comentario)
        val usernameView: TextView = itemView.findViewById(R.id.username)
        val profilePicView: ImageView = itemView.findViewById(R.id.avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comentario, parent, false)
        return TextoHolder(view)
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

    override fun onBindViewHolder(holder: TextoHolder, position: Int) {

        val elem = Lista[position]
        val comment = elem.comment
        val profilePic = elem.profilePic
        val username = elem.username

        Log.d("tag-comments", username + comment + profilePic)

        // Configurar el texto en el TextView
        holder.commentView.text = comment
        holder.usernameView.text = username
        Glide.with(holder.profilePicView)
            .load(profilePic) // Asegúrate de que CommentData tenga un campo profilePic
            .apply(RequestOptions.circleCropTransform())
            .into(holder.profilePicView)
    }


}