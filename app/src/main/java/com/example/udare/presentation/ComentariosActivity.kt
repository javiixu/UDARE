package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.CommentsAdapter
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.R
import com.example.udare.data.model.Post
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.services.interfaces.IPostService
import javax.inject.Inject

class ComentariosActivity : AppCompatActivity() {

    @Inject
    lateinit var postService: IPostService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)


        val backButton = findViewById<Button>(R.id.bAtras)
        val sendButton = findViewById<Button>(R.id.bEnviarComentario)
        val comentarioListener = findViewById<EditText>(R.id.listenerComentario)

        val Lista: MutableList<String> = mutableListOf("hola", "mundo")


        val recyclerView: RecyclerView = findViewById(R.id.RecyclerComentarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear e instanciar el adaptador
        val textoAdapter = CommentsAdapter(Lista, this)

        // Establecer el adaptador en el RecyclerView
        recyclerView.adapter = textoAdapter

        backButton.setOnClickListener(){
            Intent(this, Inicio::class.java).also{
                startActivity(it)
            }
        }

        sendButton.setOnClickListener {
            val comentario = comentarioListener.text.toString()
            comentarioListener.setText("")
            postService.addComment(THIS_POST_ID, THIS_USER_ID, comentario, object : PostRepository.callbackAddComment {
                    override fun onSuccess(post: Post) {
                        Log.d("tag-comment", "Post actualizado")
                    }

                    override fun onError(mensajeError: String?) {
                        Log.d("tag-prueba", "Error: $mensajeError")
                    }
                }
            )
        }

    }
}