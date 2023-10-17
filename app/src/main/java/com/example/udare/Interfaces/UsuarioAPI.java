package com.example.udare.Interfaces;

import com.example.udare.Modelo.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioAPI {
    @GET("/users")
    public Call<List<Usuario>> getUsers();
}
