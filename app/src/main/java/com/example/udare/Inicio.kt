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

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var notificationManager: Notificacion
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
        notificationManager = Notificacion(this)
        notificationManager.createNotification()

    }


}