package com.example.udare.api;

import com.example.udare.Modelo.Post;
import com.example.udare.Modelo.Reto;
import com.example.udare.Modelo.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @GET("/users")
    Call<List<Usuario>> getAllUsers();

    @GET("/posts")
    Call<List<Post>> getAllPosts();

    @GET("/challenges")
    Call<List<Reto>> getAllChallenges();

    @Multipart
    @POST("/posts/add")
    Call<ResponseBody> guardarPost(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part post
    );
}
