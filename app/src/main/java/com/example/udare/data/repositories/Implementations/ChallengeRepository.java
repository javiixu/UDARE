package com.example.udare.data.repositories.Implementations;

import com.example.udare.data.model.Challenge;
import com.example.udare.data.repositories.Interfaces.IChallengeRepository;
import com.example.udare.data.remote.ApiClient;
import com.example.udare.data.remote.ApiService;

import java.util.List;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeRepository implements IChallengeRepository {
    final ApiService apiService;

    @Inject
    public ChallengeRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getAllChallenges(final ChallengeCallback callback) {
        Call<List<Challenge>> call = apiService.getAllChallenges();
        call.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful()) {
                    List<Challenge> challenges = response.body();
                    if (challenges != null) {
                        callback.onSuccess(challenges);
                    } else {
                        callback.onError("Lista de retos nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public interface ChallengeCallback {
        void onSuccess(List<Challenge> challenges);
        void onError(String mensajeError);
    }
}
