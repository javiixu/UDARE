package com.example.udare.data.model;

import java.util.Date;
import java.util.List;

public class Post {
    private String userID;
    private String challengeID;
    private String caption;
    private String image;
    private Date date;
    private String _id;
    private List<CommentData> comments;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChallengeID() {
        return challengeID;
    }

    public void setChallengeID(String challengeID) {
        this.challengeID = challengeID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String get_id() {
        return _id;
    }

}
