package com.example.udare.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.udare.R
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class PerfilActivity : AppCompatActivity() {
    @Inject
    lateinit var userService: IUserService
    lateinit var thisUser: User

    @SuppressWarnings("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        //Buttons & Views
        var tvSocialPoints = findViewById<TextView>(R.id.tvSocialPoints)
        var tvSportPoints = findViewById<TextView>(R.id.tvSportPoints)
        var tvGrowthPoints = findViewById<TextView>(R.id.tvGrowthPoints)
        var tvCulturePoints = findViewById<TextView>(R.id.tvCulturePoints)
        var tvCookingPoints = findViewById<TextView>(R.id.tvCookingPoints)
        val btnUserAlbum = findViewById<ImageButton>(R.id.btnGallery)
        //val btnRankingFriends = findViewById<Button>(R.id.btnRankingFriends)
        val btnChangeProfilePicture = findViewById<ImageButton>(R.id.btnChangeProfilePicture)
        val btnBackFromProfile = findViewById<ImageButton>(R.id.btnBackFromProfile)
        var ivProfilePicture = findViewById<ImageView>(R.id.profilePicture)
        var tvUserName = findViewById<TextView>(R.id.tvUserName)
        var tvUserNameTag = findViewById<TextView>(R.id.tvUserNameTag)
        var tvUserPointsMessage = findViewById<TextView>(R.id.tvUserPointsMessage)
        var buttonFollowers = findViewById<Button>(R.id.btnFollowers)
        var buttonFollowing = findViewById<Button>(R.id.btnFollowing)

        //TODO
        // if user updates profile pic send it to database

        //get the data of the user, who is logged in and display his points, profile pic, username and bio
        thisUser = UserSingleton.obtenerInstancia().obtenerUsuario()
        tvCookingPoints.text = thisUser.profile.pointsCooking.toString()
        tvCulturePoints.text = thisUser.profile.pointsCulture.toString()
        tvSocialPoints.text = thisUser.profile.pointsSocial.toString()
        tvSportPoints.text = thisUser.profile.pointsSport.toString()
        tvGrowthPoints.text = thisUser.profile.pointsGrowth.toString()

        val userPointSum = thisUser.profile.allPoints
        val userPointsMessage = "<font color=#f9f4f1>¡Ya tienes</font> <font color=#e7ee86>$userPointSum puntos</font> <font color=#f9f4f1>!</font> "
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvUserPointsMessage.setText(Html.fromHtml(userPointsMessage, Html.FROM_HTML_MODE_LEGACY))
        } else {
           tvUserPointsMessage.setText(Html.fromHtml(userPointsMessage))
        }



        //user does not have profile pic, the standard profile pic just stays
        //otherwise we have to set it
        if(thisUser.profile.profilePic != ""){
            Log.d("tag-prueba",thisUser.profile.profilePic)
            Glide.with(this@PerfilActivity).load(thisUser.profile.profilePic).into(ivProfilePicture)

        }

        //set Bio and Username
        if(thisUser.username != null){tvUserNameTag.text = thisUser.username.toString()}
        if(thisUser.profile.nombre != null){tvUserName.text = thisUser.profile.nombre.toString()}


        //set followers and following
        buttonFollowers.text = "Followers\n" + thisUser.profile.followers.size.toString()
        buttonFollowing.text = "Following\n" + thisUser.profile.following.size.toString()






        var imageUri : Uri


        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    imageUri = uri

                    // set the profile Picture to the selected uri
                    //TODO send this profile picture to the database
                    val bitmap:Bitmap = getCapturedImage(imageUri)
                    ivProfilePicture.setImageBitmap(bitmap)

                    uploadProfilePicture(bitmap, thisUser.id)

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        btnChangeProfilePicture.setOnClickListener() {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        btnUserAlbum.setOnClickListener(){
            val intent = Intent(this@PerfilActivity, CalendarGaleryActivity::class.java)
            this@PerfilActivity.startActivity(intent)
        }


        buttonFollowers.setOnClickListener(){
            val intent = Intent(this@PerfilActivity, FollowersActivity::class.java)
            this@PerfilActivity.startActivity(intent)
        }

        buttonFollowing.setOnClickListener(){
            val intent = Intent(this@PerfilActivity, FollowingActivity::class.java)
            this@PerfilActivity.startActivity(intent)
        }

        btnBackFromProfile.setOnClickListener(){
            Intent(this, Inicio::class.java).also{
                startActivity(it)
            }
        }

        //TODO ACCESS Gallery
        //TODO Access Ranking





    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )

            else -> {
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }

    fun uploadProfilePicture(bitmap: Bitmap, userId: String?) {


        try {
            val imageName = java.text.SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale("es", "ES")).format(Date(System.currentTimeMillis())) + "_perfil"
            //val file = File(this.applicationContext.filesDir, imageName)

            var file = createFileFromBitmap(bitmap, imageName)


            userService.updateUserImage(file, thisUser, userId, object : UserRepository.callbackUpdateUserImage{
                override fun onSuccess(user: User) {
                    UserSingleton.obtenerInstancia().actualizarFoto(user.profile.profilePic)
                    Log.d("tag-prueba", "Imagen de usuario subido correctamente")
                }

                override fun onError(mensajeError: String?) {
                    Log.d("tag-prueba", "Error: $mensajeError")
                }
            })



        } catch (e: Exception) {
            Log.e("tag-foto", "Error al subir la foto: ${e.message}")
        }
    }


    fun createFileFromBitmap(bitmap: Bitmap, filename: String ) : File {

        // Get the external storage directory
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

        // Specify the file path (you can customize this based on your requirements)
        val filePath = "${storageDirectory.absolutePath}/${filename}.png"

        // Create a File object with the specified path
        val file = File(filePath)

        try {
            // Create a FileOutputStream for the File
            val outStream = FileOutputStream(file)

            // Compress the Bitmap to the OutputStream with PNG format and the specified quality
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)

            // Close the OutputStream
            outStream.close()

            // Optionally, you can use the 'file' object to get more information about the created file
            // For example, you can retrieve the absolute path: file.absolutePath

        } catch (e: IOException) {
            // Handle IOException (e.g., file creation failure)
            e.printStackTrace()
        }

        return file
    }
}

