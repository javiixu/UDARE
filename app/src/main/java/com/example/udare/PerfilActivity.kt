package com.example.udare

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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        //Buttons & Views
        var btnChangeProfilePicture: Button
        var ivProfilePicture : ImageView


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

