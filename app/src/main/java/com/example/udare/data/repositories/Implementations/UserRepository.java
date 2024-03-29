package com.example.udare.data.repositories.Implementations;

import android.util.Log;

import com.example.udare.data.model.CommentData;
import com.example.udare.data.model.Post;
import com.example.udare.data.model.User;
import com.example.udare.data.remote.ApiClient;
import com.example.udare.data.remote.ApiService;
import com.example.udare.data.repositories.Interfaces.IUserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void getUserByUid(String uid, final callbackGetUserByUid callback) {
        Call<User> call = apiService.getUserByUid(uid);
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

    public void createUser(final callbackPostUser callback, User user) {
        Call<User> call = apiService.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    Log.d("UserRepository", "onResponse: " + user);
                    if(user != null){
                        callback.onSuccess(user);
                    }else{
                        callback.onError("Usuario nulo");
                    }
                }else{
                  callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

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

//    Update User Profile Pic
    public void updateProfilePic(final callbackPostUser callback, User user, File image) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), image);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        MultipartBody.Part bodyUser = MultipartBody.Part.createFormData("user", null, RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(user)));

        Call<User> call = apiService.updateProfilePic(bodyImage, bodyUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    if(user != null){
                        callback.onSuccess(user);
                    }else{
                        callback.onError("Usuario nulo");
                    }
                }else{
                  callback.onError("Error en la respuesta: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateUserImage(File file, User user, String userId, final UserRepository.callbackUpdateUserImage callback) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MultipartBody.Part bodyUser = MultipartBody.Part.createFormData("user", null, RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(user)));

        Call<User> call = apiService.updateUserByIdImage(bodyImage,bodyUser, userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User updatedUser = response.body();
                    callback.onSuccess(updatedUser);
                } else {
                    callback.onError("Error en la respuesta de subir imagen de usuario: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public void getFollowers(String userId, final callbackGetFollowers callback) {
        Call<List<User>> call = apiService.getFollowers(userId);
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

          
    @Override
    public void getFollowing(String userId,final callbackGetFollowing callback) {
        Call<List<User>> call = apiService.getFollowing(userId);
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

    @Override
    public void getNotFollowingUsers(String userId,final callbackGetNotFollowingUsers callback) {
        Call<List<User>> call = apiService.getNotFollowingUsers(userId);
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

    @Override
    public void followUser(String userId, String userToFollow, callbackFollowUser callback) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("_id", userToFollow);
        Call<JsonObject> call = apiService.followUser(userId, requestBody); // Cambiar a JsonObject
        call.enqueue(new Callback<JsonObject>() { // Cambiar a JsonObject
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject userObject = response.body();
                    if (userObject != null && userObject.has("_id")) {
                        String userId = userObject.get("_id").getAsString();
                        callback.onSuccess(userId);
                    } else {
                        callback.onError("El objeto de usuario es nulo o no tiene un campo '_id'");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }


    @Override
    public void unfollowUser(String userId, String userToUnfollowId, callbackUnfollowUser callback) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("_id", userToUnfollowId);
        Call<JsonObject> call = apiService.unfollowUser(userId, requestBody); // Cambiar a JsonObject
        call.enqueue(new Callback<JsonObject>() { // Cambiar a JsonObject
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject userObject = response.body();
                    if (userObject != null && userObject.has("_id")) {
                        String userId = userObject.get("_id").getAsString();
                        callback.onSuccess(userId);
                    } else {
                        callback.onError("El objeto de usuario es nulo o no tiene un campo '_id'");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }


    public interface callbackGetAllUsers {
        void onSuccess(List<User> users);
        void onError(String mensajeError);
    }


    public interface callbackGetUserById {
        void onSuccess(User user);
        void onError(String mensajeError);
    }

    public interface callbackGetUserByUid {
        void onSuccess(User user);
        void onError(String mensajeError);
    }

    public interface callbackUpdateUser {
        void onSuccess(User user);
        void onError(String mensajeError);
    }


    public interface callbackPostUser {
        void onSuccess(User user);
        void onError(String mensajeError);
    }


    public interface callbackUpdateUserProfilePicture {
        void onSuccess(User user);
        void onError(String mensajeError);
    }


    public interface callbackUpdateUserImage {
        void onSuccess(User user);
        void onError(String mensajeError);
    }

    public interface callbackGetFollowers {
        void onSuccess(List<User> users);
        void onError(String mensajeError);
    }

    public interface callbackGetFollowing {
        void onSuccess(List<User> users);
        void onError(String mensajeError);
    }
    public interface callbackGetNotFollowingUsers {
        void onSuccess(List<User> users);
        void onError(String mensajeError);
    }

    public interface callbackFollowUser {
        void onSuccess(String mensaje);
        void onError(String mensajeError);
    }

    public interface callbackUnfollowUser {
        void onSuccess(String mensaje);
        void onError(String mensajeError);
    }


}
