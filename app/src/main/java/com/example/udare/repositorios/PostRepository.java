package com.example.udare.repositorios;

import com.example.udare.Modelo.Post;
import com.example.udare.api.ApiClient;
import com.example.udare.api.ApiService;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private final ApiService apiService;

    public PostRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void subirPostConImagen(File file, Post post, final PostCallback callback) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MultipartBody.Part bodyPost = MultipartBody.Part.createFormData("post", null, RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(post)));

        Call<ResponseBody> call = apiService.guardarPost(bodyImage,bodyPost);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Lógica adicional en caso de éxito
                } else {
                    callback.onError("Error en la respuesta de subir imagen: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }






    public void obtenerPosts(final PostRepository.PostCallback callback) {
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

    public interface PostCallback {
        void onSuccess(List<Post> posts);
        void onError(String mensajeError);
    }

}
