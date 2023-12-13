package com.example.udare.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer

import android.os.Handler
import android.os.Looper

import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.udare.R
import com.example.udare.data.model.CommentData
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.example.udare.data.model.Post
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.services.interfaces.IPostService
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class HacerFotoActivityMejora : AppCompatActivity() {
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var thisUser : User


    @Inject
    lateinit var userService: IUserService


    @Inject
    lateinit var postService: IPostService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hacer_foto_mejora)

        val userId = UserSingleton.obtenerInstancia().obtenerUsuario().id

        supportActionBar?.hide()

        //find all the buttons and text views
        var btnTakePhoto = findViewById<ImageView>(R.id.btnTakePhoto2)
        //var tvChoosenChallenge = findViewById<TextView>(R.id.tvChoosenChallenge)
        var btnSwitchCamera = findViewById<ImageView>(R.id.btnSwitchCamera2)
        var btnBackFromTakingPhoto = findViewById<ImageView>(R.id.btnBackFromTakingPhoto2)
        var btnFlash = findViewById<ImageView>(R.id.btnFlash);

        var tvTime = findViewById<TextView>(R.id.tvTimerChallengeCamera)

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
        doTimer(difference, tvTime)


        //start the camera
        if(checkCameraPermission(this)){
            startCamera()
        }
        else{
            requestCameraPermission(this)
        }





        //set the choosen challenge to the correct name
        //tvChoosenChallenge.text = intent.getStringExtra("EXTRA_CHOOSEN_CHALLENGE")

        //handle getting back to the main activity
        btnBackFromTakingPhoto.setOnClickListener(){
            Intent(this, SeleccionarRetoActivity::class.java).also{
                startActivity(it)
            }
        }

        val patrocina = intent.getBooleanExtra("EXTRA_PATROCINADO", false)

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
            userService.getUserById(userId, object : UserRepository.callbackGetUserById {
                override fun onSuccess(user: User) {
                    thisUser = user

                    //update that the user has completed the daily challenge
                    thisUser.dailyChallengeCompleted = true;


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
                userService.updateUser(userId, thisUser, object : UserRepository.callbackUpdateUser{
                    override fun onSuccess(user: User) {
                        Log.d("tag-HacerFotoActivity","User updated correctly")
                        UserSingleton.obtenerInstancia().actualizarPuntos(
                            user.profile.pointsSport,
                            user.profile.pointsCooking,
                            user.profile.pointsCulture,
                            user.profile.pointsGrowth,
                            user.profile.pointsSocial
                        )
                        UserSingleton.obtenerInstancia().actualizarChallengeCompleted(true)
                    }

                    override fun onError(mensajeError: String) {
                        Log.d("tag-HacerFotoActivity","Error updating user")
                    }

                })
            }, 5000)




            //set the view to the „success“ layout
            //setContentView(R.layout.activity_hacer_foto_challenge_completed)


            //returns to main and clears the activity stack after a small delay
/*
            Handler(Looper.getMainLooper()).postDelayed({
                //TODO test this further, was copied from Stack Overflow
                val intent = Intent(this, Inicio::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


                if (patrocina) {
                lateinit var notificationManager: NotificacionPatrocinado
                notificationManager = NotificacionPatrocinado(this)
                notificationManager.createNotification()
                }

                finish()
            }, 7000)
*/

            //TODO make sure the image is uploaded correctly

        }



    }

    private fun checkCameraPermission(context: HacerFotoActivityMejora): Boolean{
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission(context: HacerFotoActivityMejora){
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
                    val file = createTempFile("photo", ".jpg", ).apply {
                        output.savedUri?.let { uri ->
                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }



                    subirFoto(file)
                    val challengeCompletedLayout = layoutInflater.inflate(R.layout.activity_hacer_foto_challenge_completed, null)
                    var textView = challengeCompletedLayout.findViewById<TextView>(R.id.textView2)
                    textView.text = "Enhorabuena "+UserSingleton.obtenerInstancia().obtenerUsuario().profile.nombre+", has completado el reto de hoy!"
                    setContentView(challengeCompletedLayout)



                    Handler(Looper.getMainLooper()).postDelayed({
                        //TODO test this further, was copied from Stack Overflow
                        val intent = Intent(this@HacerFotoActivityMejora, Inicio::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }, 7000)
                }
            }
        )
    }

    fun subirFoto(file : File) {
        try {

            val comments: MutableList<CommentData> = mutableListOf()

            val post = Post()
            post.caption = "paseando por la naturaleza!!"
            post.userID = UserSingleton.obtenerInstancia().obtenerUsuario().id
            post.challengeID = "652eb4074c5c257aa8831c88"
            post.comments = comments


            postService.uploadPost(file,post,object : PostRepository.callbackUploadPost {
                override fun onSuccess(post: Post) {
                    Log.d("tag-foto", "Post subido correctamente")
                }

                override fun onError(mensajeError: String?) {
                    Log.d("tag-foto", "Error: $mensajeError")
                }
            })
        } catch (e: Exception) {
            Log.e("tag-foto", "Error al subir la foto: ${e.message}")
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

    private fun startCamera(){
        val previewView = findViewById<PreviewView>(R.id.viewFinder2)
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        previewView.controller = cameraController
    }


}
