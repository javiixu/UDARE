package com.example.udare.api;

import com.example.udare.Modelo.Post;
import com.example.udare.Modelo.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/users")
    Call<List<Usuario>> getAllUsers();

    @GET("/posts")
    Call<List<Post>> getAllPosts();
}
