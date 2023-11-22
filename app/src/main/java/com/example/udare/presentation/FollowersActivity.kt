package com.example.udare.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.CommentsAdapter
import com.example.udare.Adapter.FollowersAdapter
import com.example.udare.R
import com.example.udare.data.model.CommentData
import com.example.udare.data.model.Post
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IPostService
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FollowersActivity : AppCompatActivity() {

    @Inject
    lateinit var userService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        val userId = UserSingleton.obtenerInstancia().obtenerUsuario().id


        userService.getFollowers(userId, object : UserRepository.callbackGetFollowers {
            override fun onSuccess(users: List<User>) {
                val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFollowers)
                recyclerView.layoutManager = LinearLayoutManager(this@FollowersActivity)

                val followersAdapter = FollowersAdapter(users)
                recyclerView.adapter = followersAdapter
            }

            override fun onError(mensajeError: String?) {

            }
        })
    }
}