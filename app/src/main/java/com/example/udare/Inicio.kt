package com.example.udare



import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.Modelo.Post
import com.example.udare.Modelo.Usuario
import com.example.udare.repositorios.PostRepository
import com.example.udare.repositorios.PostRepository.PostCallback
import com.example.udare.repositorios.UsuarioRepository
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import kotlin.concurrent.thread

class Inicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


        //subirFotoPrueba()



        //Obtiene todos los usuarios del backend
        val usuarioRepository = UsuarioRepository()
        usuarioRepository.obtenerUsuarios(object : UsuarioRepository.UsuarioCallback {
            override fun onSuccess(usuariosList: MutableList<Usuario>) {
                for (usuario in usuariosList) {
                    Log.d("tag-prueba", "Nombre de usuario: ${usuario.username}")
                    /* TODO
                    if(usuario.id == 'xxxx'){
                        //set daily Challenge completed in the registered user class accordingly
                        // if the user has completed challenge do not give the option to take
                        // a foto, otherwise give him the option
                    }
                    else{
                    }
                     */
                }
            }

            override fun onError(mensajeError: String) {
                // Maneja el error en la llamada a la API, si ocurre algún error
                Log.d("tag-prueba", "Error: $mensajeError")
            }
        })




        //Obtiene todos los posts del backend
        val postRepository = PostRepository()
        postRepository.obtenerPosts((object : PostRepository.PostCallback {
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
        }))



        val popupButton = findViewById<Button>(R.id.retos)
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
            val imageName = "fotopaisaje"
            val resourceId = resources.getIdentifier(imageName, "drawable", packageName)
            val drawable = resources.getDrawable(resourceId, null)
            val bitmap = (drawable as BitmapDrawable).bitmap

            val file = File(this.applicationContext.filesDir, "fotopaisaje.jpg")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            val post = Post()
            post.caption = "paseando por la naturaleza!!"
            post.userID = "652436d13df7259a08be9f6f"
            post.challengeID = "652eb4074c5c257aa8831c88"

            val postRepository = PostRepository()
            postRepository.subirPostConImagen(file, post, object : PostRepository.PostCallback {
                override fun onSuccess(posts: MutableList<Post>?) {
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