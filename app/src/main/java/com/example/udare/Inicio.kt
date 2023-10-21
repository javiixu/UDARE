package com.example.udare

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

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

        // Verificar y crear el canal de notificación (requerido a partir de Android 8.0)
        createNotificationChannel()

        // Llamar al método para mostrar la notificación


        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            showNotification()
        } else {
            // No tienes permiso para mostrar notificaciones, toma medidas adecuadas
        }
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "mi_canal_id"
            val channelName = "Mi Canal"
            val channelDescription = "Descripción del canal"

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification() {
        val channelId = "mi_canal_id" // El mismo ID que se usó al crear el canal
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.udare_notificacion) // Icono pequeño de la notificación
            .setContentTitle("Título de la notificación")
            .setContentText("Contenido de la notificación")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridad de la notificación


        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())

    }
}