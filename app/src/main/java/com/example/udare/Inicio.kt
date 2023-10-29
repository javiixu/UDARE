package com.example.udare



import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.util.Calendar
import kotlin.concurrent.thread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.Modelo.Post
import com.example.udare.Modelo.Usuario
import com.example.udare.repositorios.PostRepository
import com.example.udare.repositorios.UsuarioRepository
import android.content.Intent



class Inicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


        val usuarioRepository = UsuarioRepository()

        //prueba llamada al backend para obtener los usuarios
        usuarioRepository.obtenerUsuarios(object : UsuarioRepository.UsuarioCallback {
            override fun onSuccess(usuariosList: MutableList<Usuario>) {
                // Procesa la lista de usuarios aquí
                for (usuario in usuariosList) {
                    // Realiza alguna operación con cada usuario, si es necesario
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

}