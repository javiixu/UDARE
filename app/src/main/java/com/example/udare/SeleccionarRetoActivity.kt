package com.example.udare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar

class SeleccionarRetoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_reto)




        //current Calendar
        var current = Calendar.getInstance()

        //Difference in Miliseconds between our current time an the end of the challenge time
        val difference : Long

        //first day of challenge
        if(current.get(Calendar.HOUR_OF_DAY) == 6){
            difference = 0
        }
        else if(current.get(Calendar.HOUR_OF_DAY) > 6){
            // set a calendar to the following date at 6 am
            var followingDate = current.clone() as Calendar

            // Set the time to 6:00 AM, on the next day
            followingDate.set(Calendar.HOUR_OF_DAY, 6)
            followingDate.set(Calendar.MINUTE, 0)
            followingDate.set(Calendar.SECOND, 0)
            followingDate.set(Calendar.MILLISECOND, 0)
            followingDate.add(Calendar.DAY_OF_YEAR, 1)

            difference = followingDate.timeInMillis - current.timeInMillis
        }
        //second day of challenge
        else{
            // set a calendar to 6 am, do not change date, we are on the second day of the challenge
            var followingDate = current.clone() as Calendar
            followingDate.set(Calendar.HOUR_OF_DAY, 6)
            followingDate.set(Calendar.MINUTE, 0)
            followingDate.set(Calendar.SECOND, 0)
            followingDate.set(Calendar.MILLISECOND, 0)

            difference = followingDate.timeInMillis - current.timeInMillis
        }

        //starts the timer for the challenge
        val tvChallengeTimer = findViewById<TextView>(R.id.tvChallengeTimer)


        doTimer(difference, tvChallengeTimer)



    }



    fun doTimer(difference : Long, tvTimer : TextView){
        var countDownTimer = object : CountDownTimer(difference, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60


                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                tvTimer.text =
                    "$elapsedHours:$elapsedMinutes:$elapsedSeconds"
            }

            //restarts the timer at 6 o clock in the morning
            override fun onFinish() {
                var diff : Long = 24 * 60 * 60 * 1000
                doTimer(diff, tvTimer)
            }
        }.start()
    }


}

