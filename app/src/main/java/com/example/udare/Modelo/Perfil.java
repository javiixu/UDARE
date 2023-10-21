package com.example.udare.Modelo;

public class Perfil {
    private String nombre;
    private String bio;
    private String urlFoto;
    private String[] followers;
    private String[] following;

    public Perfil(String nombre, String bio, String urlFoto, String[] followers, String[] following) {
        this.nombre = nombre;
        this.bio = bio;
        this.urlFoto = urlFoto;
        this.followers = followers;
        this.following = following;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
    }

}

