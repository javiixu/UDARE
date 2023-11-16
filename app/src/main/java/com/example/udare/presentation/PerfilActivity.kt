package com.example.udare.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.udare.R
import com.example.udare.data.model.Post
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class PerfilActivity : AppCompatActivity() {
    @Inject
    lateinit var userService: IUserService
    lateinit var thisUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        //Buttons & Views
        var tvSocialPoints = findViewById<TextView>(R.id.tvSocialPoints)
        var tvSportPoints = findViewById<TextView>(R.id.tvSportPoints)
        var tvGrowthPoints = findViewById<TextView>(R.id.tvGrowthPoints)
        var tvCulturePoints = findViewById<TextView>(R.id.tvCulturePoints)
        var tvCookingPoints = findViewById<TextView>(R.id.tvCookingPoints)
        val btnUserAlbum = findViewById<Button>(R.id.btnUserAlbum)
        val btnRankingFriends = findViewById<Button>(R.id.btnRankingFriends)
        val btnChangeProfilePicture = findViewById<Button>(R.id.btnChangeProfilePicture)
        var ivProfilePicture = findViewById<ImageView>(R.id.profilePicture)
        var tvUserName = findViewById<TextView>(R.id.tvUserName)
        var tvUserBio = findViewById<TextView>(R.id.tvUserBio)

        //TODO
        // if user updates profile pic send it to database

        //get the data of the user, who is logged in and display his points, profile pic, username and bio
        userService.getUserById(THIS_USER_ID, object : UserRepository.callbackGetUserById {
            override fun onSuccess(user: User) {
                thisUser = user
                tvCookingPoints.text = thisUser.profile.pointsCooking.toString()
                tvCulturePoints.text = thisUser.profile.pointsCulture.toString()
                tvSocialPoints.text = thisUser.profile.pointsSocial.toString()
                tvSportPoints.text = thisUser.profile.pointsSport.toString()
                tvGrowthPoints.text = thisUser.profile.pointsGrowth.toString()


                //user does not have profile pic, the standard profile pic just stays
                //otherwise we have to set it
                if(thisUser.profile.profilePic != "Unspecified"){
                    Log.d("tag-prueba",thisUser.profile.profilePic)
                    Glide.with(this@PerfilActivity).load(thisUser.profile.profilePic).into(ivProfilePicture)

                }

                //set Bio and Username
                tvUserBio.text = thisUser.profile.bio.toString()
                tvUserName.text = thisUser.username.toString()



            }

            override fun onError(mensajeError: String?) {
                Log.d("tag-PerfilActivity","Error in getUserById")
            }

        })







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

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        btnChangeProfilePicture.setOnClickListener() {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }







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

    fun uploadProfilePicture(bitmap: Bitmap) {
        try {
            val imageName = java.text.SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale("es", "ES"))
                .format(Date(System.currentTimeMillis())) + "_perfil"
            val file = File(this.applicationContext.filesDir, imageName)
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            //this can't work we need some way to create the aws link here
            //thisUser.profile.profilePic = file
            /*
            userService.updateUser(THIS_USER_ID, thisUser, object : UserRepository.callbackUpdateUser{
                override fun onSuccess(user: User?) {
                    TODO("Not yet implemented")
                }

                override fun onError(mensajeError: String?) {
                    TODO("Not yet implemented")
                }

            })

             */
        } catch (e: Exception) {
            Log.e("tag-foto", "Error al subir la foto: ${e.message}")
        }
    }

}

