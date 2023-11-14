package com.example.udare.data.repositories.Implementations;

import com.example.udare.data.model.Post;
import com.example.udare.data.model.User;
import com.example.udare.data.remote.ApiClient;
import com.example.udare.data.remote.ApiService;
import com.example.udare.data.repositories.Interfaces.IUserRepository;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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


    public void getUserById(String userId, final callbackGetUserById callback) {
        Call<User> call = apiService.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError("Usuario nulo");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateUser(String userId, User updatedUser, callbackUpdateUser callback) {
        Call<User> call = apiService.updateUserById(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError("Usuario actualizado nulo");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }









    public interface callbackGetAllUsers {
        void onSuccess(List<User> users);
        void onError(String mensajeError);
    }

    public interface callbackUpdateUser {
        void onSuccess(User user);
        void onError(String mensajeError);
    }

    public interface callbackGetUserById {
        void onSuccess(User user);
        void onError(String mensajeError);
    }
}
