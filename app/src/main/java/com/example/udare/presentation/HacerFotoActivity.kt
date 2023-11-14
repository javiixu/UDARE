package com.example.udare.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.udare.R
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class HacerFotoActivity : AppCompatActivity() {
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var thisUser : User


    @Inject
    lateinit var userService: IUserService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hacer_foto)

        //find all the buttons and text views
        var btnTakePhoto = findViewById<Button>(R.id.btnTakePhoto)
        var tvChoosenChallenge = findViewById<TextView>(R.id.tvChoosenChallenge)
        var btnSwitchCamera = findViewById<Button>(R.id.btnSwitchCamera)
        var btnBackFromTakingPhoto = findViewById<Button>(R.id.btnBackFromTakingPhoto)

        //set the choosen challenge to the correct name
        tvChoosenChallenge.text = intent.getStringExtra("EXTRA_CHOOSEN_CHALLENGE")

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


            //get the data of the user, who is logged in and modify his points
            userService.getUserById(THIS_USER_ID, object : UserRepository.callbackGetUserById {
                override fun onSuccess(user: User) {
                    thisUser = user

                    //update that the user has completed the daily challenge
                    //TODO ACTIVATE THIS AGAIN
                    //thisUser.dailyChallengeCompleted = true


                    //update the users points in the according challenge
                    val categoryChallenge =  intent.getStringExtra("EXTRA_CATEGORY_CHALLENGE")
                    when (categoryChallenge) {
                        "sport" -> thisUser.profile.pointsSport += 100
                        "culture" -> thisUser.profile.pointsCulture += 100
                        "social" -> thisUser.profile.pointsSocial += 100
                        "cooking" -> thisUser.profile.pointsCooking += 100
                        "growth" -> thisUser.profile.pointsGrowth += 100
                        else -> { // Note the block
                            print("ERROR: reto no esta en una categoria")
                        }
                    }

                    Log.d("tag-HacerFotoActivity","getUserById was successful")

                }

                override fun onError(mensajeError: String?) {
                    Log.d("tag-HacerFotoActivity","Error in getUserById")
                }

            })



            //only after 5 sec update User in DB
            Handler(Looper.getMainLooper()).postDelayed({
                if(thisUser == null){
                    Log.d("tag-HacerFotoActivity","UPDATE USER PROBLEM, user is 0")
                }
                //update user in DB
                userService.updateUser(THIS_USER_ID, thisUser, object : UserRepository.callbackUpdateUser{
                    override fun onSuccess(user: User) {
                        Log.d("tag-HacerFotoActivity","User updated correctly")
                    }

                    override fun onError(mensajeError: String) {
                        Log.d("tag-HacerFotoActivity","Error updating user")
                    }

                })
            }, 5000)




            //set the view to the „success“ layout
            setContentView(R.layout.activity_hacer_foto_challenge_completed)


            //returns to main and clears the activity stack after a small delay
            Handler(Looper.getMainLooper()).postDelayed({
                //TODO test this further, was copied from Stack Overflow
                val intent = Intent(this, Inicio::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }, 7000)


            //TODO make sure the image is uploaded correctly

        }



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