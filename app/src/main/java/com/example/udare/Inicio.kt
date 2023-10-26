package com.example.udare

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.AlarmManager
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
import java.util.Calendar
import kotlin.concurrent.thread
import kotlin.system.exitProcess

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
        thread {
            checkAndShowNotification()
        }


    }
    private fun checkAndShowNotification() {
        val desiredHour = 12
        val desiredMinute = 0
        lateinit var notificationManager: Notificacion
        notificationManager = Notificacion(this)
        var toSound = true
        while (toSound) {
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentTime.get(Calendar.MINUTE)

            if (currentHour == desiredHour && currentMinute == desiredMinute) {
                notificationManager.createNotification()

                toSound = false
            }

            Thread.sleep(1000)
        }
    }

}