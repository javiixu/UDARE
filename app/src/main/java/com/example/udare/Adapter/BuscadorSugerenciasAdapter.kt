package com.example.udare.Adapter


import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView

class BuscadorSugerenciasAdapter(private val Lista: List<User>,
                                 private val context: Context,
                                 private val userService: IUserService
) :
    RecyclerView.Adapter<BuscadorSugerenciasAdapter.TextoHolder>(){

    private val estadoImagenes: MutableMap<Int, Boolean> = mutableMapOf()

    class TextoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameView: TextView = itemView.findViewById(R.id.username_sugerencia)
        val profilePicView: ImageView = itemView.findViewById(R.id.foto_user_sugerencia)
        val botonAñadirAmigo: ImageView = itemView.findViewById(R.id.boton_añadir_amigo)
        val nameView: TextView = itemView.findViewById(R.id.name_sugerencia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sugerencias, parent, false)
        return TextoHolder(view)
    }

    override fun getItemCount(): Int {
        return Lista.size
    }

    override fun onBindViewHolder(holder: TextoHolder, position: Int) {

        val usuario = UserSingleton.obtenerInstancia().obtenerUsuario()
        val elem = Lista[position]
        val profilePic = elem.profile.profilePic
        val username = elem.username
        val name = elem.profile.name

        Log.d("tag-comments", username + profilePic)

        holder.usernameView.text = "@" + username
        holder.nameView.text = name
        Glide.with(holder.profilePicView)
            .load(profilePic) // Asegúrate de que CommentData tenga un campo profilePic
            .apply(RequestOptions.circleCropTransform())
            .into(holder.profilePicView)

        estadoImagenes[position] = true

        holder.botonAñadirAmigo.setOnClickListener(){


            if (estadoImagenes[position] == true) {

                holder.botonAñadirAmigo.setImageResource(R.drawable.boton_delete)
                estadoImagenes[position] = false

                userService.followUser(usuario.id, elem.id, object : UserRepository.callbackFollowUser {
                    override fun onSuccess(message: String) {}
                    override fun onError(mensajeError: String?) {}
                })

            } else {

                holder.botonAñadirAmigo.setImageResource(R.drawable.boton_add)
                estadoImagenes[position] = true

                userService.unfollowUser(usuario.id, elem.id, object : UserRepository.callbackUnfollowUser {
                    override fun onSuccess(message: String) {}
                    override fun onError(mensajeError: String?) {}
                })
            }
        }

    }



}