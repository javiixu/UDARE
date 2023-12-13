package com.example.udare.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import com.example.udare.R
import com.example.udare.data.model.Post
import com.example.udare.data.model.Reaction
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton

import com.example.udare.services.interfaces.IUserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Date
import java.util.Locale
import javax.inject.Inject

//Import PostRepository
import com.example.udare.data.repositories.Implementations.ReactionRepository
import com.example.udare.services.interfaces.IReactionService

//import com.example.udare.services.interfaces.IReactionService


@AndroidEntryPoint
class ReactionActivity : AppCompatActivity() {
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var currentUser : FirebaseUser
    private lateinit var user : User
    private lateinit var auth: FirebaseAuth
    private lateinit var uid : String
    private lateinit var postId : String

    @Inject
    lateinit var userService: IUserService

    //TODO: Reaction Service
    @Inject
    lateinit var reactionService: IReactionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val camaraLayout = layoutInflater.inflate(R.layout.activity_hacer_foto_mejora_reaccion, null)

        setContentView(camaraLayout)

        supportActionBar?.hide()

        postId = intent.getStringExtra("postId").toString()
        uid = intent.getStringExtra("userLogged").toString()

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        //find all the buttons and text views
        var btnTakePhoto = findViewById<ImageView>(R.id.btnTakePhoto3)
        var btnSwitchCamera = findViewById<ImageView>(R.id.btnSwitchCamera3)
        var btnBackFromTakingPhoto = findViewById<ImageView>(R.id.btnBackFromTakingPhoto3)

        //handle getting back to the main activity
        btnBackFromTakingPhoto.setOnClickListener(){
            Intent(this, Inicio::class.java).also{
                startActivity(it)
            }
        }

        //start the camera
        if(checkCameraPermission(this)){
            startCamera()
        }
        else{
            requestCameraPermission(this)
        }

//        startCamera()

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

        //handle clicks on the take photo button
        btnTakePhoto.setOnClickListener(){
            takePhoto(uid)
        }
    }

    private fun checkCameraPermission(context: ReactionActivity): Boolean{
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission(context: ReactionActivity){
        ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.CAMERA), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED){
                startCamera()
            }
            else{
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera(){
        val previewView = findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder3)
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        previewView.controller = cameraController
    }

    private fun takePhoto(userId: String?) {
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
                    Log.e("ReactionActivity", "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d("ReactionActivity", msg)
                    val file = createTempFile("photo", ".jpeg", ).apply {
                        output.savedUri?.let { uri ->
                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }


                    subirFoto(file, uid, postId)
//                    setContentView(R.layout.activity_reaction_success)
                    Handler(Looper.getMainLooper()).postDelayed({
                        //TODO test this further, was copied from Stack Overflow
                        val intent = Intent(this@ReactionActivity, Inicio::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }, 7000)
                }
            }
        )
    }

    fun subirFoto(file : File, userID: String?, postID: String?) {
        try {

            val reaction = Reaction(userID,postID)

            reactionService.uploadReaction(file, reaction, object : ReactionRepository.callbackUploadReaction {

                override fun onSuccess(reaction: Reaction?) {
                    Log.i("tag-foto", "Foto subida correctamente")
                }

                override fun onError(mensajeError: String?) {
                    Log.e("tag-foto", "Error al subir la foto 1: $mensajeError")

                }
            })
        } catch (e: Exception) {
            Log.e("tag-foto", "Error al subir la foto 2: ${e.message}")
        }


    }
}