package com.example.udare.presentation



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.R
import com.example.udare.data.model.Post
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.presentation.Notificacion
import com.example.udare.presentation.PerfilActivity
import com.example.udare.presentation.SeleccionarRetoActivity
import com.example.udare.services.interfaces.IChallengeService
import com.example.udare.services.interfaces.IPostService
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
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


        //Buttons & Views
        val btnTestPerfil = findViewById<Button>(R.id.btnTestPerfil)
        val popupButton = findViewById<Button>(R.id.challenges)







        //Obtiene todos los usuarios del backend
        userService.getAllUsers(object : UserRepository.callbackGetAllUsers {
            override fun onSuccess(users: List<User>) {
                for (usuario in users) {
                    Log.d("tag-prueba", "Nombre de usuario: ${usuario.username}")


                    //the user id  of the app owner is set to David S. id for testing
                    if(usuario.getId() == THIS_USER_ID ){

                        // if the user has completed challenge do not give the option to take
                        // a foto, otherwise give him the option

                        //if user has completed the daily challenge disable the popup button
                        if(usuario.dailyChallengeCompleted){
                            popupButton.visibility = View.GONE
                        }
                    }



                }
            }

            override fun onError(mensajeError: String?) {
                Log.d("tag-prueba", "Error: $mensajeError")

            }
        })


        postService.getAllPosts(object : PostRepository.callbackGetAllPosts {
            override fun onSuccess(posts: MutableList<Post>) {

                val photoRecyclerView: RecyclerView = findViewById(R.id.RecyclerFotos)

               /* val fotoList = mutableListOf<String>()

                for(post in posts){
                    fotoList.add(post.image)
                }*/

                val photoAdapter = FotoAdapter(posts, this@Inicio)
                photoRecyclerView.adapter = photoAdapter
                photoRecyclerView.layoutManager = LinearLayoutManager(this@Inicio)

            }

            override fun onError(mensajeError: String?) {

            }
        })


        popupButton.setOnClickListener(){
            Intent(this, SeleccionarRetoActivity::class.java).also{
                startActivity(it)
            }
        }



        btnTestPerfil.setOnClickListener(){
            Intent(this, PerfilActivity::class.java).also{
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






}
