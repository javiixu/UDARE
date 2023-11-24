package com.example.udare.Adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R
import com.example.udare.data.model.PostData
import com.example.udare.data.model.Reaction
import com.example.udare.data.repositories.Implementations.ReactionRepository
import com.example.udare.presentation.ComentariosActivity
import com.example.udare.presentation.ReactionActivity
import com.example.udare.services.implementations.ReactionService
import com.example.udare.services.interfaces.IReactionService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

//@AndroidEntryPoint
class FotoAdapter(private val Lista: List<PostData>, private val uid: String?, private val context: Context, private val reactionService: IReactionService) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {


//    private var selectedEmoji = -1

class FotoAdapter(private val Lista: List<PostData>, private val context: Context) : RecyclerView.Adapter<FotoAdapter.FotoHolder>() {


    private val circularImagesAdapter = mutableListOf<CircularImagesAdapter>()
    class FotoHolder(itemView: View, Lista: List<PostData>, public val reactionService: IReactionService) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.foto_viewer)
        val textViewClick: TextView = itemView.findViewById(R.id.comentarios)
        val usernameView: TextView = itemView.findViewById(R.id.username)
        val avatarView: ImageView = itemView.findViewById(R.id.avatar)

        val captureButton = itemView.findViewById<Button>(R.id.capture_button)

        var reactionList = mutableListOf<Reaction>()

        val circularImageView : RecyclerView = itemView.findViewById(R.id.circularImagesRecyclerView)
        var circularImagesAdapter : CircularImagesAdapter? = null

//        @Inject



//        init {
//            Log.d("FotoAdapter", "init: " + Lista.size)
////            Log.d("FotoAdapter", "init: " + Lista[bindingAdapterPosition].post._id)
//            Log.d("FotoAdapter", "init: " + bindingAdapterPosition)
//            if (bindingAdapterPosition != RecyclerView.NO_POSITION){
//                reactionService.getReactionsByPost(Lista[bindingAdapterPosition].post._id,object : ReactionRepository.callbackGetReactionsByPost {
//                    override fun onSuccess(reactions: MutableList<Reaction>?) {
//                        reactionList = reactions ?: mutableListOf()
//                        Log.d("FotoAdapter", "onSuccess: " + reactionList.size)
//                    }
//
//                    override fun onError(mensajeError: String?) {
//                        Log.d("FotoAdapter", "onError: " + mensajeError)
//                    }
//
//                })
//
//                val circularImageView: RecyclerView = itemView.findViewById(R.id.circularImagesRecyclerView)
//                val circularImagesAdapter = CircularImagesAdapter(reactionList)
//
//                circularImageView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
//                circularImageView.adapter = circularImagesAdapter
//            }
//
//
//        }

        val Lista = Lista
        init {
            captureButton.setOnClickListener {
                // Do something in response to button click
                dispatchTakePictureIntent()
            }
        }

        protected fun dispatchTakePictureIntent() {
//            Go to the activity ReactionActivity
            val intent = Intent(itemView.context, ReactionActivity::class.java)
//            Get the post id and send it to the ReactionActivity
            intent.putExtra("postId", Lista[bindingAdapterPosition].post._id)
            intent.putExtra("userLogged", Lista[bindingAdapterPosition].post.userID)
            Log.d("FotoAdapter", "dispatchTakePictureIntent: " + Lista[bindingAdapterPosition].post._id)
            Log.d("FotoAdapter", "dispatchTakePictureIntent: " + Lista[bindingAdapterPosition].post.userID)
            itemView.context.startActivity(intent)
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_foto, parent, false)
        return FotoHolder(view, Lista, reactionService)
    }

    override fun onBindViewHolder(holder: FotoHolder, position: Int) {

        val standardSize = 1200 // Tamaño estándar en píxeles

        val options = RequestOptions()
            .override(standardSize, standardSize)
            .centerCrop()

        Glide.with(holder.imageView)
            .load(Lista[position].post.image)
            .apply(options)
            .into(holder.imageView)

        holder.usernameView.text = Lista[position].username
        Glide.with(holder.avatarView)
            .load(Lista[position].profilePic) // Asegúrate de que CommentData tenga un campo profilePic
            .apply(RequestOptions.circleCropTransform())
            .into(holder.avatarView)

        holder.textViewClick.setOnClickListener {
            // Crear un Intent para iniciar la nueva actividad (HacerFotoActivity)
            val comments = Lista[position].post.comments
            val intent = Intent(context, ComentariosActivity::class.java)
            intent.putExtra("postId", Lista[position].post._id)
            intent.putExtra("comments", ArrayList<Any>(comments))
            context.startActivity(intent)
        }

        if(holder.circularImagesAdapter == null){
            holder.circularImagesAdapter = CircularImagesAdapter(holder.reactionList)
            holder.circularImageView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.circularImageView.adapter = holder.circularImagesAdapter
        }

        reactionService.getReactionsByPost(Lista[position].post._id,object : ReactionRepository.callbackGetReactionsByPost {
            override fun onSuccess(reactions: MutableList<Reaction>?) {
                holder.circularImagesAdapter?.updateList(reactions ?: mutableListOf())
            }

            override fun onError(mensajeError: String?) {
                Log.d("FotoAdapter", "onError: " + mensajeError)
            }

        })

//        if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION){
//            Log.d("FotoAdapter", "init: " + Lista.size)
//            Log.d("FotoAdapter", "init: " + Lista[position].post._id)
//            reactionService.getReactionsByPost(Lista[holder.bindingAdapterPosition].post._id,object : ReactionRepository.callbackGetReactionsByPost {
//                override fun onSuccess(reactions: MutableList<Reaction>?) {
//                    holder.reactionList = reactions ?: mutableListOf()
//                    Log.d("FotoAdapter", "onSuccess: " + holder.reactionList.size)
//                }
//
//                override fun onError(mensajeError: String?) {
//                    Log.d("FotoAdapter", "onError: " + mensajeError)
//                }
//
//            })
//
//        }

//        Log.d("FotoAdapter", "init: " + Lista.size)
////            Log.d("FotoAdapter", "init: " + Lista[bindingAdapterPosition].post._id)
//        Log.d("FotoAdapter", "init: " + bindingAdapterPosition)
//        if (bindingAdapterPosition != RecyclerView.NO_POSITION){
//            reactionService.getReactionsByPost(Lista[bindingAdapterPosition].post._id,object : ReactionRepository.callbackGetReactionsByPost {
//                override fun onSuccess(reactions: MutableList<Reaction>?) {
//                    reactionList = reactions ?: mutableListOf()
//                    Log.d("FotoAdapter", "onSuccess: " + reactionList.size)
//                }
//
//                override fun onError(mensajeError: String?) {
//                    Log.d("FotoAdapter", "onError: " + mensajeError)
//                }
//
//            })
//
//            val circularImageView: RecyclerView = itemView.findViewById(R.id.circularImagesRecyclerView)
//            val circularImagesAdapter = CircularImagesAdapter(reactionList)
//
//            circularImageView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
//            circularImageView.adapter = circularImagesAdapter

    }

    override fun getItemCount(): Int {
        return Lista.size
    }


//    fun onEmojiButtonClick(view: View) {
//        // Do something in response to button click
//        when(view.id){
//            R.id.emoji1Button -> {
//                // Do something
//                selectedEmoji = 1
//            }
//            R.id.emoji2Button -> {
//                // Do something
//                selectedEmoji = 2
//            }
//            R.id.emoji3Button -> {
//                // Do something
//                selectedEmoji = 3
//            }
//            R.id.emoji4Button -> {
//                // Do something
//                selectedEmoji = 4
//            }
//            R.id.emoji5Button -> {
//                // Do something
//                selectedEmoji = 5
//            }
//            else -> {
//                // Do something
//                selectedEmoji = -1
//            }
//        }
//
//        if (selectedEmoji != -1) {
//            // Do something
//        }
//    }

}
