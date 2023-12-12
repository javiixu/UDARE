package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.Adapter.BuscadorSeguidorAdapter
import com.example.udare.Adapter.BuscadorSiguiendoAdapter
import com.example.udare.Adapter.BuscadorSugerenciasAdapter
import com.example.udare.R
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

        val textoBuscador = findViewById<EditText>(R.id.buscar_usuario)
        val recyclerSugerencias = findViewById<RecyclerView>(R.id.RecyclerBuscador)
        val backButton = findViewById<ImageView>(R.id.back_buscador_amigos)
        val sugerenciasButton = findViewById<Button>(R.id.boton_sugerencias)
        val seguidoresButton = findViewById<Button>(R.id.boton_seguidores)
        val siguiendoButton = findViewById<Button>(R.id.boton_siguiendo)
        val fotoPerfil = findViewById<ImageView>(R.id.foto_perfil_buscador)
        val usuario = UserSingleton.obtenerInstancia().obtenerUsuario()
        var Lista : List<User> = emptyList()

        Glide.with(fotoPerfil)
            .load(
                UserSingleton.obtenerInstancia().obtenerUsuario().profile.profilePic
            ) // Asegúrate de que CommentData tenga un campo profilePic
            .apply(RequestOptions.circleCropTransform())
            .into(fotoPerfil)

        var sugerenciasAdapter = BuscadorSugerenciasAdapter(Lista, this@BuscadorUsuario, userService)
        recyclerSugerencias.adapter = sugerenciasAdapter


        userService.getNotFollowingUsers(
            usuario.id,
            object : UserRepository.callbackGetNotFollowingUsers {
                override fun onSuccess(ListaSugerencias: List<User>) {
                    Lista = ListaSugerencias
                    sugerenciasAdapter =
                        BuscadorSugerenciasAdapter(ListaSugerencias, this@BuscadorUsuario, userService)
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

            userService.getNotFollowingUsers(
                usuario.id,
                object : UserRepository.callbackGetNotFollowingUsers {
                    override fun onSuccess(ListaSugerencias: List<User>) {
                        Lista = ListaSugerencias
                        sugerenciasAdapter =
                            BuscadorSugerenciasAdapter(ListaSugerencias, this@BuscadorUsuario, userService)
                        recyclerSugerencias.adapter = sugerenciasAdapter
                    }

                    override fun onError(mensajeError: String?) {

                    }
                })

        }

        seguidoresButton.setOnClickListener() {
            setButtons(seguidoresButton, sugerenciasButton, siguiendoButton)

            userService.getFollowers(usuario.id, object : UserRepository.callbackGetFollowers {
                override fun onSuccess(ListaSeguidores: List<User>) {
                    Lista = ListaSeguidores
                    val seguidorAdapter =
                        BuscadorSeguidorAdapter(ListaSeguidores, this@BuscadorUsuario)
                    recyclerSugerencias.adapter = seguidorAdapter
                }

                override fun onError(mensajeError: String?) {

                }
            })
        }

        siguiendoButton.setOnClickListener() {
            setButtons(siguiendoButton, seguidoresButton, sugerenciasButton)

            userService.getFollowing(usuario.id, object : UserRepository.callbackGetFollowing {
                override fun onSuccess(ListaSiguiendo: List<User>) {
                    Lista = ListaSiguiendo
                    val siguiendoAdapter =
                        BuscadorSiguiendoAdapter(ListaSiguiendo, this@BuscadorUsuario, userService)
                    recyclerSugerencias.adapter = siguiendoAdapter
                }

                override fun onError(mensajeError: String?) {

                }
            })
        }


        textoBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Filtrar la lista de sugerencias según el texto en el EditText
                val ListaFiltrada = Lista.filter { it.username.startsWith(s) }
                if (!ListaFiltrada.isEmpty()) {
                    Log.d("tag-eo", ListaFiltrada[0].username)
                    val sugerenciasAdapter =
                        BuscadorSugerenciasAdapter(ListaFiltrada, this@BuscadorUsuario, userService)
                    recyclerSugerencias.adapter = sugerenciasAdapter
                } else{
                    recyclerSugerencias.adapter = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fun updateSugerenciaList(){
            userService.getNotFollowingUsers(
                usuario.id,
                object : UserRepository.callbackGetNotFollowingUsers {
                    override fun onSuccess(ListaSugerencias: List<User>) {
                        Lista = ListaSugerencias
                        sugerenciasAdapter =
                            BuscadorSugerenciasAdapter(ListaSugerencias, this@BuscadorUsuario, userService)
                        recyclerSugerencias.adapter = sugerenciasAdapter
                    }

                    override fun onError(mensajeError: String?) {
                    }
                })
        }
    }

    fun setButtons(buttonPressed: Button, buttonUnpressed1: Button, buttonUnpressed2: Button) {
        buttonPressed.setBackgroundResource(R.drawable.botones_buscador_pressed)
        buttonPressed.setTextColor(Color.parseColor("#171717"))

        buttonUnpressed1.setBackgroundResource(R.drawable.botones_buscador)
        buttonUnpressed1.setTextColor(Color.parseColor("#F9F4F1"))

        buttonUnpressed2.setBackgroundResource(R.drawable.botones_buscador)
        buttonUnpressed2.setTextColor(Color.parseColor("#F9F4F1"))
    }


}

