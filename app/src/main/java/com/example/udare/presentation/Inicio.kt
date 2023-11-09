package com.example.udare.presentation



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.data.model.Post
import com.example.udare.data.model.User
import com.example.udare.R
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.implementations.UserService
import com.example.udare.services.interfaces.IChallengeService
import com.example.udare.services.interfaces.IPostService
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject
import kotlin.concurrent.thread

@AndroidEntryPoint
class Inicio : AppCompatActivity() {

    @Inject
    lateinit var postService: IPostService

    @Inject
    lateinit var challengeService : IChallengeService

    @Inject
    lateinit var userService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


        //subirFotoPrueba()


        //Obtiene todos los usuarios del backend
        userService.getAllUsers(object : UserRepository.callbackGetAllUsers {
            override fun onSuccess(users: List<User>) {
                for (usuario in users) {
                    Log.d("tag-prueba", "Nombre de usuario: ${usuario.username}")
                }
            }

            override fun onError(mensajeError: String?) {
                Log.d("tag-prueba", "Error: $mensajeError")

            }
        })


        postService.getAllPosts(object : PostRepository.callbackGetAllPosts {
            override fun onSuccess(posts: MutableList<Post>) {

                val photoRecyclerView: RecyclerView = findViewById(R.id.viewer)

                val fotoList = mutableListOf<String>()

                for(post in posts){
                    fotoList.add(post.image)
                }

                val photoAdapter = FotoAdapter(fotoList)
                photoRecyclerView.adapter = photoAdapter
                photoRecyclerView.layoutManager = LinearLayoutManager(this@Inicio)

            }

            override fun onError(mensajeError: String?) {

            }
        })

        val popupButton = findViewById<Button>(R.id.challenges)
        popupButton.setOnClickListener(){
            Intent(this, SeleccionarRetoActivity::class.java).also{
                startActivity(it)
            }

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


    fun subirFotoPrueba() {
        try {
            val imageName = "foto2"
            val resourceId = resources.getIdentifier(imageName, "drawable", packageName)
            val drawable = resources.getDrawable(resourceId, null)
            val bitmap = (drawable as BitmapDrawable).bitmap

            val file = File(this.applicationContext.filesDir, "foto2.jpg")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            val post = Post()
            post.caption = "paseando por la naturaleza!!"
            post.userID = "652436d13df7259a08be9f6f"
            post.challengeID = "652eb4074c5c257aa8831c88"


            postService.uploadPost(file,post,object : PostRepository.callbackUploadPost {
                override fun onSuccess(post: Post) {
                    Log.d("tag-prueba", "Post subido correctamente")
                }

                override fun onError(mensajeError: String?) {
                    Log.d("tag-prueba", "Error: $mensajeError")
                }
            })


        } catch (e: Exception) {
            Log.e("tag-foto", "Error al subir la foto: ${e.message}")
        }
    }

}