package com.example.udare.data.repositories.Implementations;

import com.example.udare.data.model.CommentData;
import com.example.udare.data.model.Post;
import com.example.udare.data.remote.ApiClient;
import com.example.udare.data.remote.ApiService;
import com.example.udare.data.repositories.Interfaces.IPostRepository;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostRepository implements IPostRepository {
    final ApiService apiService;

    @Inject
    public PostRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void uploadPost(File file, Post post, final PostRepository.callbackUploadPost callback) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MultipartBody.Part bodyPost = MultipartBody.Part.createFormData("post", null, RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(post)));

        Call<Post> call = apiService.uploadPost(bodyImage,bodyPost);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post uploadedPost = response.body();
                    callback.onSuccess(uploadedPost);
                } else {
                    callback.onError("Error en la respuesta de subir imagen: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }



    @Override
    public void getAllPosts(final PostRepository.callbackGetAllPosts callback) {
        Call<List<Post>> call = apiService.getAllPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    if (posts != null) {
                        callback.onSuccess(posts);
                    } else {
                        callback.onError("Lista de posts nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void addComment(String postId, String userId, String comment, final callbackAddComment callback) {
        CommentData commentData = new CommentData(userId, comment);
        Call<Post> call = apiService.addComment(postId, commentData);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post uploadedPost = response.body();
                    callback.onSuccess(uploadedPost);
                } else {
                    callback.onError("Error en la respuesta de subir comentario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public interface callbackAddComment{
        void onSuccess(Post post);
        void onError(String mensajeError);
    }

    public interface callbackGetAllPosts {
        void onSuccess(List<Post> posts);
        void onError(String mensajeError);
    }

    public interface callbackUploadPost {
        void onSuccess(Post post);
        void onError(String mensajeError);
    }

}
