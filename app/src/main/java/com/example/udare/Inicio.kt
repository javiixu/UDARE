package com.example.udare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FotoAdapter

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val popupButton = findViewById<Button>(R.id.retos)
        val popupView = LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.BLACK))
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true

        popupButton.setOnClickListener {
            popupWindow.showAtLocation(popupButton, Gravity.BOTTOM, 0, 600)
        }

        popupView.setOnTouchListener { _ , _ ->
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
            true
        }

        val photoRecyclerView: RecyclerView = findViewById(R.id.viewer)

        val fotoList = listOf(
                R.drawable.foto1,
                R.drawable.foto2,
                R.drawable.foto3

                // Agrega más imágenes aquí
        )

        val photoAdapter = FotoAdapter(fotoList)
        photoRecyclerView.adapter = photoAdapter
        photoRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}