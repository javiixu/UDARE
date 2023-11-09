package com.example.udare.data.model;

public class User {
    private String username;
    private String password;
    private String email;
    private String[] posts;
    private Profile profile;

    private Boolean dailyChallengeCompleted;

    private String _id;

    public User(String username, String password, String email, String[] posts, Profile profile,
                Boolean dailyChallengeCompleted, String _id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.posts = posts;
        this.profile = profile;
        this.dailyChallengeCompleted = dailyChallengeCompleted;
        this._id = _id;

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

    public Profile getPerfil() {
        return profile;
    }

    public void setPerfil(Profile profile) {
        this.profile = profile;
    }

    public Boolean getDailyChallengeCompleted() {
        return dailyChallengeCompleted;
    }

    public void setDailyChallengeCompleted(Boolean dailyChallengeCompleted) {
        this.dailyChallengeCompleted = dailyChallengeCompleted;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }
}
