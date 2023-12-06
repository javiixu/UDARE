package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.Adapter.BuscadorSugerenciasAdapter
import com.example.udare.Adapter.FollowersAdapter
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.R
import com.example.udare.data.model.PostData
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class BuscadorUsuario : AppCompatActivity() {

    @Inject
    lateinit var userService: IUserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador_usuario)

        supportActionBar?.hide()

        val backButton = findViewById<ImageView>(R.id.back_buscador_amigos)
        val sugerenciasButton = findViewById<Button>(R.id.boton_sugerencias)
        val seguidoresButton = findViewById<Button>(R.id.boton_seguidores)
        val siguiendoButton = findViewById<Button>(R.id.boton_siguiendo)
        val recyclerSugerencias = findViewById<RecyclerView>(R.id.RecyclerBuscador)
        val fotoPerfil = findViewById<ImageView>(R.id.foto_perfil_buscador)
        val Lista: MutableList<User> = mutableListOf()
        val usuario = UserSingleton.obtenerInstancia().obtenerUsuario()

        Glide.with(fotoPerfil)
            .load(UserSingleton.obtenerInstancia().obtenerUsuario().profile.profilePic) // Asegúrate de que CommentData tenga un campo profilePic
            .apply(RequestOptions.circleCropTransform())
            .into(fotoPerfil)

        recyclerSugerencias.layoutManager = LinearLayoutManager(this)
        var sugerenciasAdapter = BuscadorSugerenciasAdapter(Lista, this)
        recyclerSugerencias.adapter = sugerenciasAdapter

        userService.getAllUsers(object : UserRepository.callbackGetAllUsers {
            override fun onSuccess(ListaSugerencias: List<User>) {
                sugerenciasAdapter = BuscadorSugerenciasAdapter(ListaSugerencias, this@BuscadorUsuario)
                recyclerSugerencias.adapter = sugerenciasAdapter
            }

            override fun onError(mensajeError: String?) {

            }
        })

        backButton.setOnClickListener() {
            Intent(this, Inicio::class.java).also {
                startActivity(it)
            }
        }

        sugerenciasButton.setOnClickListener() {
            setButtons(sugerenciasButton, seguidoresButton, siguiendoButton)

            userService.getAllUsers(object : UserRepository.callbackGetAllUsers {
                override fun onSuccess(ListaSugerencias: List<User>) {
                    sugerenciasAdapter = BuscadorSugerenciasAdapter(ListaSugerencias, this@BuscadorUsuario)
                    recyclerSugerencias.adapter = sugerenciasAdapter
                }

                override fun onError(mensajeError: String?) {

                }
            })

            sugerenciasAdapter = BuscadorSugerenciasAdapter(Lista, this)
            recyclerSugerencias.adapter = sugerenciasAdapter
        }

        seguidoresButton.setOnClickListener() {
            setButtons(seguidoresButton, sugerenciasButton, siguiendoButton)

            userService.getFollowers(usuario.id, object : UserRepository.callbackGetFollowers {
                override fun onSuccess(users: List<User>) {
                    sugerenciasAdapter = BuscadorSugerenciasAdapter(users, this@BuscadorUsuario)
                    recyclerSugerencias.adapter = sugerenciasAdapter
                }

                override fun onError(mensajeError: String?) {

                }
            })
        }

        siguiendoButton.setOnClickListener() {
            setButtons(siguiendoButton, seguidoresButton, sugerenciasButton)

            userService.getFollowing(usuario.id, object : UserRepository.callbackGetFollowing {
                override fun onSuccess(ListaSiguiendo: List<User>) {
                    sugerenciasAdapter = BuscadorSugerenciasAdapter(ListaSiguiendo, this@BuscadorUsuario)
                    recyclerSugerencias.adapter = sugerenciasAdapter
                }

                override fun onError(mensajeError: String?) {

                }
            })
        }




    }

    fun setButtons(buttonPressed: Button, buttonUnpressed1: Button, buttonUnpressed2: Button){
        buttonPressed.setBackgroundResource(R.drawable.botones_buscador_pressed)
        buttonPressed.setTextColor(Color.parseColor("#171717"))

        buttonUnpressed1.setBackgroundResource(R.drawable.botones_buscador)
        buttonUnpressed1.setTextColor(Color.parseColor("#F9F4F1"))

        buttonUnpressed2.setBackgroundResource(R.drawable.botones_buscador)
        buttonUnpressed2.setTextColor(Color.parseColor("#F9F4F1"))
    }
}