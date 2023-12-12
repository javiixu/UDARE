package com.example.udare.data.model;

public class CalendarData {
    public Post post;
    public Challenge challenge;
    public String orientation; // = "landscape" or = "portrait"

    public CalendarData(Post post, Challenge challenge, String orientation) {
        this.post = post;
        this.challenge = challenge;
        this.orientation = orientation;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
