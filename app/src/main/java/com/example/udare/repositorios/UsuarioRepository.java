package com.example.udare.repositorios;

import com.example.udare.Modelo.Post;
import com.example.udare.Modelo.Usuario;
import com.example.udare.api.ApiClient;
import com.example.udare.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private final ApiService apiService;

    public UsuarioRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void obtenerUsuarios(final UsuarioCallback callback) {
        Call<List<Usuario>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    if (usuarios != null) {
                        callback.onSuccess(usuarios);
                    } else {
                        callback.onError("Lista de usuarios nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public interface UsuarioCallback {
        void onSuccess(List<Usuario> usuarios);
        void onError(String mensajeError);
    }
}
