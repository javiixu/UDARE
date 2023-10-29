package com.example.udare.repositorios;

import com.example.udare.Modelo.Reto;
import com.example.udare.Modelo.Usuario;
import com.example.udare.api.ApiClient;
import com.example.udare.api.ApiService;

import java.util.List;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetoRepository {
    private final ApiService apiService;

    public RetoRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void obtenerRetos(final RetoCallback callback) {
        Call<List<Reto>> call = apiService.getAllChallenges();
        call.enqueue(new Callback<List<Reto>>() {
            @Override
            public void onResponse(Call<List<Reto>> call, Response<List<Reto>> response) {
                if (response.isSuccessful()) {
                    List<Reto> retos = response.body();
                    if (retos != null) {
                        callback.onSuccess(retos);
                    } else {
                        callback.onError("Lista de retos nula");
                    }
                } else {
                    callback.onError("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reto>> call, Throwable t) {
                callback.onError("Error en la llamada: " + t.getMessage());
            }
        });
    }

    public interface RetoCallback {
        void onSuccess(List<Reto> retos);
        void onError(String mensajeError);
    }
}
