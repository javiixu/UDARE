package com.example.udare.data.repositories.Implementations;

import com.example.udare.data.model.User;
import com.example.udare.data.remote.ApiClient;
import com.example.udare.data.remote.ApiService;
import com.example.udare.data.repositories.Interfaces.IUserRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository implements IUserRepository {
    final ApiService apiService;

    @Inject
    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getAllUsers(final callbackGetAllUsers callback) {
        Call<List<User>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    if (users != null) {
                        callback.onSuccess(users);
                    } else {
                        callback.onError("Lista de usuarios nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public interface callbackGetAllUsers {
        void onSuccess(List<User> users);
        void onError(String mensajeError);
    }
}
