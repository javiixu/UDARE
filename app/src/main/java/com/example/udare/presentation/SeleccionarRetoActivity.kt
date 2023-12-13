package com.example.udare.presentation

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    lateinit var challengeService: IChallengeService

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO Check in Database if the challenge has already been done

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_reto)

        supportActionBar?.hide()


        //toolbar
        val backButton = findViewById<ImageView>(R.id.back_buscador_amigos)
        val fotoPerfil = findViewById<ImageView>(R.id.foto_perfil_buscador)

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
        var btnPatrocinado = findViewById<Button>(R.id.btnPatrocinado)

        var retosDeportes = mutableListOf<Challenge>()
        var retosSocial = mutableListOf<Challenge>()
        var retosCultura = mutableListOf<Challenge>()
        var retosCrecimientoPersonal = mutableListOf<Challenge>()
        var retosCocina = mutableListOf<Challenge>()


        challengeService.getAllChallenges(object : ChallengeRepository.ChallengeCallback {
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
                btnSocialChallenge.text = retosSocial.get(0).title + "\n" + "SOCIAL"
                btnCultureChallenge.text = retosCultura.get(0).title + "\n" + "CULTURA"
                btnSportChallenge.text = retosDeportes.get(0).title + "\n" + "DEPORTE"
                btnCookingChallenge.text = retosCocina.get(0).title + "\n" + "COCINAR"
                btnGrowthChallenge.text =
                    retosCrecimientoPersonal.get(0).title + "\n" + "CRECIMIENTO PERSONAL"

            }

            override fun onError(mensajeError: String?) {
                Log.d("tag-prueba", "Error: $mensajeError")
            }
        })

        var choosenChallenge: String


        //challenge going back to main activity
        backButton.setOnClickListener() {
            Intent(this, Inicio::class.java).also {
                startActivity(it)
            }
        }


        fotoPerfil.setOnClickListener() {
            Intent(this, PerfilActivity::class.java).also {
                startActivity(it)
            }
        }

        //for every challenge handle what happens when this challenge gets clicked
        btnCookingChallenge.setOnClickListener() {
            choosenChallenge = btnCookingChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivityMejora::class.java).also {
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE", choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "cooking")
                startActivity(it)
            }
        }

        btnGrowthChallenge.setOnClickListener() {
            choosenChallenge = btnGrowthChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivityMejora::class.java).also {
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE", choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "growth")
                startActivity(it)
            }
        }



        btnSocialChallenge.setOnClickListener() {
            choosenChallenge = btnSocialChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivityMejora::class.java).also {
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE", choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "social")
                startActivity(it)
            }
        }



        btnCultureChallenge.setOnClickListener() {
            choosenChallenge = btnCultureChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivityMejora::class.java).also {
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE", choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "culture")
                startActivity(it)
            }
        }


        btnSportChallenge.setOnClickListener() {
            choosenChallenge = btnSportChallenge.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivityMejora::class.java).also {
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE", choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "sport")
                startActivity(it)
            }
        }



        btnPatrocinado.setOnClickListener() {
            choosenChallenge = btnPatrocinado.text.toString().substringBefore("\n")
            Intent(this, HacerFotoActivityMejora::class.java).also {
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE", choosenChallenge)
                it.putExtra("EXTRA_CATEGORY_CHALLENGE", "patrocinado")
                startActivity(it)
            }
        }


    }


}



