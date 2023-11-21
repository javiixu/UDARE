package com.example.udare.data.model;

import java.io.Serializable;

public class CommentData implements Serializable {

    public String _id;
    public String comment;
    public String profilePic;
    public String username;
    public String userId;

    public CommentData(String userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public CommentData(String comment, String profilePic, String username){
        this.comment = comment;
        this.profilePic = profilePic;
        this.username = username;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}