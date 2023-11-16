package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.CommentsAdapter
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.R

class ComentariosActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val backButton = findViewById<Button>(R.id.bAtras)

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
    }
}