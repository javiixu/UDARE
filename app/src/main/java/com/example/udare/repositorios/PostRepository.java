package com.example.udare.repositorios;

import com.example.udare.Modelo.Post;
import com.example.udare.api.ApiClient;
import com.example.udare.api.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private final ApiService apiService;

    public PostRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
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
