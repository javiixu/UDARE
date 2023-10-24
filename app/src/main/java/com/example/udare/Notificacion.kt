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
import androidx.core.content.ContextCompat.getSystemService

class Notificacion(val context: Context) {
    companion object {
        const val CHANNELID = "channel"
    }

    init{
        createChannel()
    }
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNELID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createNotification() {
        val builder = NotificationCompat.Builder(context, CHANNELID)
            .setSmallIcon(androidx.core.R.drawable.notification_bg) // Icono pequeño de la notificación
            .setContentTitle("Título de la notificación")
            .setContentText("Contenido de la notificación")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación


        with(NotificationManagerCompat.from(context)) {
            notify(1,builder.build())
        }

    }
}