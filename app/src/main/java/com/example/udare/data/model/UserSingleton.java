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
}
