package com.example.udare.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.udare.R
import com.example.udare.data.model.User
import de.hdodenhof.circleimageview.CircleImageView

class FollowersAdapter(private val followersList: List<User>) : RecyclerView.Adapter<FollowersAdapter.TextoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false)
        return TextoHolder(view)
    }

    override fun onBindViewHolder(holder: TextoHolder, position: Int) {
        val user = followersList[position]
        holder.usernameTextView.text = user.username;

        Glide.with(holder.itemView.context)
            .load(user.profile.profilePic)
            .into(holder.avatarImageView)
    }

    override fun getItemCount(): Int {
        return followersList.size
    }

    class TextoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: CircleImageView = itemView.findViewById(R.id.avatar)
        val usernameTextView: TextView = itemView.findViewById(R.id.username)
    }

}