package com.example.udare.Modelo;

public class Usuario {
    private String username;
    private String password;
    private String email;
    private String[] posts;
    private Perfil perfil;

    public Usuario(String username, String password, String email, String[] posts, Perfil perfil) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.posts = posts;
        this.perfil = perfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getPosts() {
        return posts;
    }

    public void setPosts(String[] posts) {
        this.posts = posts;
    }


}
