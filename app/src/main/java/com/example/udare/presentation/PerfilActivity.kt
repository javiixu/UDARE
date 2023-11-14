package com.example.udare.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import com.example.udare.R
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
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
        var btnChangeProfilePicture: Button
        var ivProfilePicture : ImageView
        val tvSocialPoints = findViewById<TextView>(R.id.tvSocialPoints)
        val tvSportPoints = findViewById<TextView>(R.id.tvSportPoints)
        val tvGrowthPoints = findViewById<TextView>(R.id.tvGrowthPoints)
        val tvCulturePoints = findViewById<TextView>(R.id.tvCulturePoints)
        val tvCookingPoints = findViewById<TextView>(R.id.tvCookingPoints)
        val btnUserAlbum = findViewById<Button>(R.id.btnUserAlbum)
        val btnRankingFriends = findViewById<Button>(R.id.btnRankingFriends)

        //TODO
        //check if user has profile picture
        //if not set default profile picture
        // if user updates profile pic send it to database

        //get the data of the user, who is logged in and display his points
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
                    ivProfilePicture =  (thisUser.profile.profilePic) as ImageView
                }


            }

            override fun onError(mensajeError: String?) {
                Log.d("tag-PerfilActivity","Error in getUserById")
            }

        })







        var imageUri : Uri

        // initializing Profile Picture and Change profile Button
        btnChangeProfilePicture = findViewById(R.id.btnChangeProfilePicture)
        ivProfilePicture = findViewById(R.id.profilePicture)




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
                    //check if there is a profile picture set in the database in the start of the activity
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

}

