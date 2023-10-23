package com.example.udare

import android.annotation.SuppressLint
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
import android.content.Intent
import android.app.PendingIntent

class Inicio : AppCompatActivity() {
    companion object {
        const val CHANNELID = "channel"
    }
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

        createChannel()
        createNotification()

    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNELID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createNotification() {
        val intent = Intent(this, Inicio::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this, 0, intent, flag)

        val builder = NotificationCompat.Builder(this, CHANNELID)
            .setSmallIcon(androidx.core.R.drawable.notification_bg) // Icono pequeño de la notificación
            .setContentTitle("Título de la notificación")
            .setContentText("Contenido de la notificación")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación


        with(NotificationManagerCompat.from(this)) {
            notify(1,builder.build())
        }

    }
}