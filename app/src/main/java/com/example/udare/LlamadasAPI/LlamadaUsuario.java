package com.example.udare.LlamadasAPI;

import android.util.Log;

import com.example.udare.Interfaces.UsuarioAPI;
import com.example.udare.Modelo.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LlamadaUsuario {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    UsuarioAPI apiUsuario = retrofit.create(UsuarioAPI.class);

    Call<List<Usuario>> call = apiUsuario.getUsers();



}
