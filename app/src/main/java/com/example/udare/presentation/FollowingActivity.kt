package com.example.udare.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FollowersAdapter
import com.example.udare.Adapter.FollowingAdapter
import com.example.udare.R
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FollowingActivity : AppCompatActivity() {
    @Inject
    lateinit var userService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        val userId = UserSingleton.obtenerInstancia().obtenerUsuario().id

        userService.getFollowing(userId, object : UserRepository.callbackGetFollowing {
            override fun onSuccess(users: List<User>) {
                val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFollowing)
                recyclerView.layoutManager = LinearLayoutManager(this@FollowingActivity)

                val followingAdapter = FollowingAdapter(users)
                recyclerView.adapter = followingAdapter
            }

            override fun onError(mensajeError: String?) {

            }
        })
    }



}