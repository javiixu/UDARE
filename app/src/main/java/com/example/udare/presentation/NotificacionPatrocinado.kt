package com.example.udare.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

public class NotificacionPatrocinado(val context: Context) {
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
                description = "DESCRIPTION"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createNotification() {
        val builder = NotificationCompat.Builder(context, CHANNELID)
            .setSmallIcon(androidx.core.R.drawable.notification_bg) // Icono pequeño de la notificación
            .setContentTitle("¡Felicidades! Aquí está tu código de descuento:")
            .setContentText("12345678")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación


        with(NotificationManagerCompat.from(context)) {
            notify(1,builder.build())
        }

    }
}
