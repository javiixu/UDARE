package com.example.udare.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.presentation.HacerFotoActivity
import com.example.udare.data.model.Challenge
import com.example.udare.R
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.ChallengeRepository
import com.example.udare.services.implementations.ChallengeService
import com.example.udare.services.interfaces.IChallengeService
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class SeleccionarRetoActivity : AppCompatActivity() {
    //variables for cameraX
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    private lateinit var cameraController: LifecycleCameraController

    @Inject
    lateinit var challengeService : IChallengeService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO Check in Database if the challenge has already been done

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_reto)

        supportActionBar?.hide()
        //TIMER MANAGMENT
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


        //CAMERA MANGAMENT
        if(allPermissionsGranted()){
            startCamera()
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 0)
        }

        //toolbar
        val backButton = findViewById<ImageView>(R.id.back_buscador_amigos)
        val fotoPerfil = findViewById<ImageView>(R.id.foto_perfil_buscador)
        val usuario = UserSingleton.obtenerInstancia().obtenerUsuario()
        Glide.with(fotoPerfil)
            .load(
                UserSingleton.obtenerInstancia().obtenerUsuario().profile.profilePic
            )
            .apply(RequestOptions.circleCropTransform())
            .into(fotoPerfil)

        //BUTTON workflow
        var btnSocialChallenge = findViewById<Button>(R.id.btnSocialChallenge)
        var btnCultureChallenge = findViewById<Button>(R.id.btnCultureChallenge)
        var btnSportChallenge = findViewById<Button>(R.id.btnSportChallenge)
        var btnCookingChallenge = findViewById<Button>(R.id.btnCookingChallenge)
        var btnGrowthChallenge = findViewById<Button>(R.id.btnGrowthChallenge)
        var btnBackFromChallengeSelect = findViewById<Button>(R.id.btnBackFromChallengeSelect)
        var btnPatrocinado = findViewById<Button>(R.id.btnPatrocinado)

        var retosDeportes  = mutableListOf<Challenge>()
        var retosSocial = mutableListOf<Challenge>()
        var retosCultura = mutableListOf<Challenge>()
        var retosCrecimientoPersonal = mutableListOf<Challenge>()
        var retosCocina = mutableListOf<Challenge>()

        val fondo = findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder)
        fondo.setBackgroundColor(Color.DKGRAY)

        val shapeSocial = GradientDrawable()
        shapeSocial.cornerRadius = 70f
        shapeSocial.setColor(android.graphics.Color.parseColor("#6A1B9A"))
        btnSocialChallenge.background = shapeSocial
        val iconoSocial = findViewById<ImageView>(R.id.iconoSocial)
        val paramsSocial = iconoSocial.layoutParams as ConstraintLayout.LayoutParams
        paramsSocial.startToStart = btnSocialChallenge.id // Alinea el final del ImageView al inicio del Button
        paramsSocial.topToTop = btnSocialChallenge.id // Alinea la parte superior del ImageView a la parte superior del Button
        paramsSocial.bottomToBottom = btnSocialChallenge.id // Alinea la parte inferior del ImageView a la parte inferior del Button
        paramsSocial.marginStart = 0 // Establece el margen que desees
        iconoSocial.layoutParams = paramsSocial
        iconoSocial.elevation = 10f

        val shapeCulture = GradientDrawable()
        shapeCulture.cornerRadius = 70f
        shapeCulture.setColor(android.graphics.Color.parseColor("#D32F2F")) // Color del fondo
        btnCultureChallenge.background = shapeCulture
        val iconoCultura = findViewById<ImageView>(R.id.iconoCultura)
        val paramsCultura = iconoCultura.layoutParams as ConstraintLayout.LayoutParams
        paramsCultura.startToStart = btnCultureChallenge.id // Alinea el final del ImageView al inicio del Button
        paramsCultura.topToTop = btnCultureChallenge.id // Alinea la parte superior del ImageView a la parte superior del Button
        paramsCultura.bottomToBottom = btnCultureChallenge.id // Alinea la parte inferior del ImageView a la parte inferior del Button
        paramsCultura.marginStart = 0 // Establece el margen que desees
        iconoCultura.layoutParams = paramsCultura
        iconoCultura.elevation = 10f

        val shapeSport = GradientDrawable()
        shapeSport.cornerRadius = 70f
        shapeSport.setColor(android.graphics.Color.parseColor("#FFA000")) // Color del fondo
        btnSportChallenge.background = shapeSport
        val iconoDeporte = findViewById<ImageView>(R.id.iconoDeporte)
        val paramsDeporte = iconoDeporte.layoutParams as ConstraintLayout.LayoutParams
        paramsDeporte.startToStart = btnSportChallenge.id // Alinea el final del ImageView al inicio del Button
        paramsDeporte.topToTop = btnSportChallenge.id // Alinea la parte superior del ImageView a la parte superior del Button
        paramsDeporte.bottomToBottom = btnSportChallenge.id // Alinea la parte inferior del ImageView a la parte inferior del Button
        paramsDeporte.marginStart = 0 // Establece el margen que desees
        iconoDeporte.layoutParams = paramsDeporte
        iconoDeporte.elevation = 10f

        val shapeCooking = GradientDrawable()
        shapeCooking.cornerRadius = 70f
        shapeCooking.setColor(android.graphics.Color.parseColor("#388E3C")) // Color del fondo
        btnCookingChallenge.background = shapeCooking
        val iconoCocina = findViewById<ImageView>(R.id.iconoCocina)
        val params = iconoCocina.layoutParams as ConstraintLayout.LayoutParams
        params.startToStart = btnCookingChallenge.id // Alinea el final del ImageView al inicio del Button
        params.topToTop = btnCookingChallenge.id // Alinea la parte superior del ImageView a la parte superior del Button
        params.bottomToBottom = btnCookingChallenge.id // Alinea la parte inferior del ImageView a la parte inferior del Button
        params.marginStart = 0 // Establece el margen que desees
        iconoCocina.layoutParams = params
        iconoCocina.elevation = 10f

        val shapeGrowth = GradientDrawable()
        shapeGrowth.cornerRadius = 70f
        shapeGrowth.setColor(android.graphics.Color.parseColor("#1976D2")) // Color del fondo
        btnGrowthChallenge.background = shapeGrowth
        val iconoCrecimiento = findViewById<ImageView>(R.id.iconoCrecimiento)
        val paramsCrecimiento = iconoCrecimiento.layoutParams as ConstraintLayout.LayoutParams
        paramsCrecimiento.startToStart = btnGrowthChallenge.id // Alinea el final del ImageView al inicio del Button
        paramsCrecimiento.topToTop = btnGrowthChallenge.id // Alinea la parte superior del ImageView a la parte superior del Button
        paramsCrecimiento.bottomToBottom = btnGrowthChallenge.id // Alinea la parte inferior del ImageView a la parte inferior del Button
        paramsCrecimiento.marginStart = 0 // Establece el margen que desees
        iconoCrecimiento.layoutParams = paramsCrecimiento
        iconoCrecimiento.elevation = 10f

        val shapePatrocinado = GradientDrawable()
        shapePatrocinado.cornerRadius = 70f
        shapePatrocinado.setColor(android.graphics.Color.parseColor("#000000"))
        btnPatrocinado.background = shapePatrocinado
        val iconoPatrocinado = findViewById<ImageView>(R.id.iconoPatrocinado)
        val paramsPatrocinado = iconoPatrocinado.layoutParams as ConstraintLayout.LayoutParams
        paramsPatrocinado.startToStart = btnPatrocinado.id // Alinea el final del ImageView al inicio del Button
        paramsPatrocinado.topToTop = btnPatrocinado.id // Alinea la parte superior del ImageView a la parte superior del Button
        paramsPatrocinado.bottomToBottom = btnPatrocinado.id // Alinea la parte inferior del ImageView a la parte inferior del Button
        paramsPatrocinado.marginStart = 0 // Establece el margen que desees
        iconoPatrocinado.layoutParams = paramsPatrocinado
        iconoPatrocinado.elevation = 10f

        challengeService.getAllChallenges(object: ChallengeRepository.ChallengeCallback {
            override fun onSuccess(challenges: List<Challenge>) {
                for (reto in challenges) {
                    when (reto.category) {
                        "deportes" -> retosDeportes.add(reto)
                        "cultura" -> retosCultura.add(reto)
                        "social" -> retosSocial.add(reto)
                        "cocina" -> retosCocina.add(reto)
                        "crecimientopersonal" -> retosCrecimientoPersonal.add(reto)
                        else -> { // Note the block
                            print("ERROR: reto no esta en una categoria")
                        }
                    }
                }
                /*btnSocialChallenge.text = retosSocial.get(0).title + "\n" + "SOCIAL"
                btnCultureChallenge.text = retosCultura.get(0).title + "\n" + "CULTURA"
                btnSportChallenge.text = retosDeportes.get(0).title + "\n" + "DEPORTE"
                btnCookingChallenge.text = retosCocina.get(0).title + "\n" + "COCINAR"
                btnGrowthChallenge.text = retosCrecimientoPersonal.get(0).title + "\n" +"CRECIMIENTO PERSONAL"*/


            }
            override fun onError(mensajeError: String?) {
                Log.d("tag-prueba", "Error: $mensajeError")
            }
        })

        var choosenChallenge : String


        //challenge going back to main activity
        backButton.setOnClickListener(){
            finish()
        }
        btnBackFromChallengeSelect.setOnClickListener(){
            finish()
        }

        //for every challenge handle what happens when this challenge gets clicked
        btnCookingChallenge.setOnClickListener(){
            choosenChallenge = btnCookingChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "cooking")
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE_OBJ",retosCocina.get(0))
                startActivity(it)
            }

        }

        btnGrowthChallenge.setOnClickListener(){
            choosenChallenge = btnGrowthChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "growth")
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE_OBJ",retosCrecimientoPersonal.get(0))
                startActivity(it)
            }
        }

        btnSocialChallenge.setOnClickListener(){
            choosenChallenge = btnSocialChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "social")
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE_OBJ",retosSocial.get(0))
                startActivity(it)
            }
        }

        btnCultureChallenge.setOnClickListener(){
            choosenChallenge = btnCultureChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "culture")
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE_OBJ",retosCultura.get(0))
                startActivity(it)
            }
       }

        btnSportChallenge.setOnClickListener(){
            choosenChallenge = btnSportChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "sport")
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE_OBJ",retosDeportes.get(0))
                startActivity(it)
            }
        }

        btnPatrocinado.setOnClickListener(){
            choosenChallenge = btnPatrocinado.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "patrocinado")
                startActivity(it)
            }
        }



    }


    //function to manage timer
    private fun doTimer(difference : Long, tvTimer : TextView){
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

    //start the camera preview
    private fun startCamera(){
        val previewView = findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder)
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        //previewView.controller = cameraController


    }


    //check if all necessary permissions are granted
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}

