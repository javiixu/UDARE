package com.example.udare


import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.Modelo.Post
import com.example.udare.Modelo.Usuario
import com.example.udare.repositorios.PostRepository
import com.example.udare.repositorios.UsuarioRepository
import android.content.Intent


class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


        val usuarioRepository = UsuarioRepository()

        //prueba llamada al backend para obtener los usuarios
        usuarioRepository.obtenerUsuarios(object : UsuarioRepository.UsuarioCallback {
            override fun onSuccess(usuariosList: MutableList<Usuario>) {
                // Procesa la lista de usuarios aquí
                for (usuario in usuariosList) {
                    // Realiza alguna operación con cada usuario, si es necesario
                    Log.d("tag-prueba", "Nombre de usuario: ${usuario.username}")
                }
            }

            override fun onError(mensajeError: String) {
                // Maneja el error en la llamada a la API, si ocurre algún error
                Log.d("tag-prueba", "Error: $mensajeError")
            }
        })


        val postRepository = PostRepository()

        postRepository.obtenerPosts((object : PostRepository.PostCallback {
            override fun onSuccess(posts: MutableList<Post>) {

                val photoRecyclerView: RecyclerView = findViewById(R.id.viewer)

                val fotoList = mutableListOf<String>()

                for(post in posts){
                    fotoList.add(post.image)
                }

                val photoAdapter = FotoAdapter(fotoList)
                photoRecyclerView.adapter = photoAdapter
                photoRecyclerView.layoutManager = LinearLayoutManager(this@Inicio)

            }

            override fun onError(mensajeError: String?) {

            }
        }))


        val popupButton = findViewById<Button>(R.id.retos)


        popupButton.setOnClickListener(){
            Intent(this, SeleccionarRetoActivity::class.java).also{
                startActivity(it)
            }

        }

    }
}