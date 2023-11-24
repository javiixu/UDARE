package com.example.udare.data.repositories.Implementations;

import android.util.Log;

import com.example.udare.data.model.Reaction;
import com.example.udare.data.remote.ApiService;
import com.example.udare.data.repositories.Interfaces.IReactionRepository;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReactionRepository implements IReactionRepository {
    final ApiService apiService;

    @Inject
    public ReactionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void addReaction(String userID, String postID, String reaction, final ReactionRepository.callbackAddReaction callback) {
        Reaction reactionObj = new Reaction(userID, postID, reaction);
        Call<Reaction> call = apiService.addReaction(reactionObj);
        call.enqueue(new Callback<Reaction>() {
            @Override
            public void onResponse(Call<Reaction> call, Response<Reaction> response) {
                if (response.isSuccessful()) {
                    Reaction addedReaction = response.body();
                    callback.onSuccess(addedReaction);
                } else {
                    callback.onError("Error en la respuesta de subir imagen: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Reaction> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void deleteReaction(String userID, String postID, final ReactionRepository.callbackDeleteReaction callback) {
        Call<Reaction> call = apiService.deleteReaction(userID, postID);
        call.enqueue(new Callback<Reaction>() {
            @Override
            public void onResponse(Call<Reaction> call, Response<Reaction> response) {
                if (response.isSuccessful()) {
                    Reaction deletedReaction = response.body();
                    callback.onSuccess(deletedReaction);
                } else {
                    callback.onError("Error en la respuesta de subir imagen: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Reaction> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void getReactions(String postID, final ReactionRepository.callbackGetReactions callback) {
        Call<List<Reaction>> call = apiService.getReactions(postID);
        call.enqueue(new Callback<List<Reaction>>() {
            @Override
            public void onResponse(Call<List<Reaction>> call, Response<List<Reaction>> response) {
                if (response.isSuccessful()) {
                    List<Reaction> reactions = response.body();
                    if (reactions != null) {
                        callback.onSuccess(reactions);
                    } else {
                        callback.onError("Lista de reactions nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reaction>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void uploadReaction(File file, Reaction reaction, final ReactionRepository.callbackUploadReaction callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        Log.d("uploadReaction", "uploadReaction: " + file.getName() + " " + file.getAbsolutePath());
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MultipartBody.Part bodyReaction = MultipartBody.Part.createFormData("reaction", null, RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(reaction)));

        Call<Reaction> call = apiService.uploadReaction(bodyImage,bodyReaction);
        call.enqueue(new Callback<Reaction>() {
            @Override
            public void onResponse(Call<Reaction> call, Response<Reaction> response) {
                if (response.isSuccessful()) {
                    Reaction uploadedReaction = response.body();
                    callback.onSuccess(uploadedReaction);
                } else {
                    callback.onError("Error en la respuesta de subir imagen: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Reaction> call, Throwable t) {
                // Manejar el error
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    @Override
    public void getReactionsByPost(String postID, final ReactionRepository.callbackGetReactions callback) {
        Call<List<Reaction>> call = apiService.getReactionsByPost(postID);
        call.enqueue(new Callback<List<Reaction>>() {
            @Override
            public void onResponse(Call<List<Reaction>> call, Response<List<Reaction>> response) {
                if (response.isSuccessful()) {
                    List<Reaction> reactions = response.body();
                    if (reactions != null) {
                        callback.onSuccess(reactions);
                    } else {
                        callback.onError("Lista de reactions nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reaction>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public interface callbackAddReaction {
        void onSuccess(Reaction reaction);
        void onError(String mensajeError);
    }

    public interface callbackDeleteReaction {
        void onSuccess(Reaction reaction);
        void onError(String mensajeError);
    }

    public interface callbackGetReactions {
        void onSuccess(List<Reaction> reactions);
        void onError(String mensajeError);
    }

    public interface callbackUploadReaction {
        void onSuccess(Reaction reaction);
        void onError(String mensajeError);
    }

    public interface callbackGetReactionsByPost {
        void onSuccess(List<Reaction> reactions);
        void onError(String mensajeError);
    }

}
