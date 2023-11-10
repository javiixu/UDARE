package com.example.udare

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.udare.R
import com.example.udare.Modelo.Usuario
import com.example.udare.repositorios.UsuarioRepository
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale

class HacerFotoActivity : AppCompatActivity() {
    private lateinit var cameraController: LifecycleCameraController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hacer_foto)

        //find all the buttons and text views
        var btnTakePhoto = findViewById<Button>(R.id.btnTakePhoto)
        var tvChoosenChallenge = findViewById<TextView>(R.id.tvChoosenChallenge)
        var btnSwitchCamera = findViewById<Button>(R.id.btnSwitchCamera)
        var btnBackFromTakingPhoto = findViewById<Button>(R.id.btnBackFromTakingPhoto)

        //handle getting back to the main activity
        btnBackFromTakingPhoto.setOnClickListener(){
            Intent(this, Inicio::class.java).also{
                startActivity(it)
            }
        }


        //start the Camera in this view to
        startCamera()


        //handle clicks on the switch camera button
        btnSwitchCamera.setOnClickListener(){
            //Front Camera was active before
            if(cameraController.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA){
                cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            }
            else{
                cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            }
        }

        //handle
        btnTakePhoto.setOnClickListener(){
            takePhoto()
            //TODO how can we retun to main here????


            //TODO setting dailyChallenge completed for this user in the database
            // how???

            //finish()
        }

        //set the choosen challenge to the correct name
        tvChoosenChallenge.text = intent.getStringExtra("EXTRA_CHOOSEN_CHALLENGE")

    }


    private fun takePhoto() {
        // Create time stamped name and MediaStore entry.
        val name = java.text.SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale("es", "ES"))
            .format(Date(System.currentTimeMillis()))
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        cameraController.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("SeleccionarRetoActivity", "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d("SeleccionarRetoActivity", msg)
                }
            }
        )
    }

    private fun startCamera(){
        val previewView = findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder)
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        previewView.controller = cameraController
    }
}