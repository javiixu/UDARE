package com.example.udare.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udare.Adapter.CommentsAdapter
import com.example.udare.R
import com.example.udare.data.model.CommentData
import com.example.udare.data.model.Post
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IPostService
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ComentariosActivity : AppCompatActivity() {

    @Inject
    lateinit var postService: IPostService

    @Inject
    lateinit var userService: IUserService

    private val _commentList = MutableLiveData<List<CommentData>>()
    val commentList: LiveData<List<CommentData>> get() = _commentList

    fun updateCommentList(newList: List<CommentData>) {
        _commentList.value = newList
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)


        val postId = intent.getStringExtra("postId")
        val comments = intent.getSerializableExtra("comments") as List<CommentData>


        val Lista: MutableList<CommentData> = mutableListOf()

        comments.forEach { commentData ->
            val userId = commentData.userId
            val comment = commentData.comment


            userService.getUserById(userId, object : UserRepository.callbackGetUserById {
                override fun onSuccess(user: User) {
                    val profilePic = user.profile.profilePic
                    val username = user.username
                    val elem = CommentData(comment, profilePic, username)
                    Lista.add(elem)
                    updateCommentList(Lista)
                    Log.d("tag-comments","" + Lista)
                }
                override fun onError(mensajeError: String?) {
                    Log.d("tag-comments","Error in getUserById")
                }
            })

        }

        commentList.observe(this, Observer { updatedList ->
            // Actualiza tu RecyclerView o cualquier otra vista aquí con la nueva lista
            val recyclerView: RecyclerView = findViewById(R.id.RecyclerComentarios)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val textoAdapter = CommentsAdapter(updatedList, this)
            recyclerView.adapter = textoAdapter
        })

        val backButton = findViewById<Button>(R.id.bAtras)
        val sendButton = findViewById<Button>(R.id.bEnviarComentario)
        val comentarioListener = findViewById<EditText>(R.id.listenerComentario)

        backButton.setOnClickListener(){
            Intent(this, Inicio::class.java).also{
                startActivity(it)
            }
        }

        sendButton.setOnClickListener {
            val comentario = comentarioListener.text.toString()
            comentarioListener.setText("")
            hideKeyboard(comentarioListener)
            postService.addComment(postId, THIS_USER_ID, comentario, object : PostRepository.callbackAddComment {
                    override fun onSuccess(post: Post) {
                        userService.getUserById(THIS_USER_ID, object : UserRepository.callbackGetUserById {
                            override fun onSuccess(user: User) {
                                val profilePic = user.profile.profilePic
                                val username = user.username
                                val elem = CommentData(comentario, profilePic, username)
                                Lista.add(elem)
                                updateCommentList(Lista)
                            }

                            override fun onError(mensajeError: String?) {
                                Log.d("tag-comments", "Error in getUserById")
                            }
                        })
                        Log.d("tag-comment", "Comentario subido")
                    }

                    override fun onError(mensajeError: String?) {
                        Log.d("tag-comment", "Error: $mensajeError")
                    }
                }
            )
        }

    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
