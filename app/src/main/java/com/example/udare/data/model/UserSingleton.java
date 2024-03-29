package com.example.udare.data.model;

import android.util.Log;

public class UserSingleton {
    private static UserSingleton instancia;
    private User usuario;

    private UserSingleton() {
        // Constructor privado para evitar la creación de instancias directas
    }

    public static UserSingleton obtenerInstancia() {
        if (instancia == null) {
            instancia = new UserSingleton();
        }
        return instancia;
    }

    public void iniciarSesion(User usuario) {
        this.usuario = usuario;
        Log.d("tag-singleton", "Usuario creado");
    }

    public void cerrarSesion() {
        usuario = null;
    }

    public User obtenerUsuario() {
        return this.usuario;
    }

    public void actualizarFoto(String url)
    {
        this.usuario.getProfile().setProfilePic(url);
    }

    public void actualizarPuntos(int pointsSport, int pointsCooking, int pointsCulture, int pointsGrowth, int pointsSocial)
    {
        this.usuario.getProfile().setPointsSport(pointsSport);
        this.usuario.getProfile().setPointsCooking(pointsCooking);
        this.usuario.getProfile().setPointsCulture(pointsCulture);
        this.usuario.getProfile().setPointsGrowth(pointsGrowth);
        this.usuario.getProfile().setPointsSocial(pointsSocial);
    }

    public void actualizarChallengeCompleted(boolean completed)
    {
        this.usuario.setDailyChallengeCompleted(completed);
    }


}
