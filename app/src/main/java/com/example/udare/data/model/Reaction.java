package com.example.udare.data.model;

import java.io.Serializable;

public class Reaction implements Serializable {
    private String userId;
    private String postId;
    private String imageURL;
    private String _id;

    public Reaction(String userId, String postId, String reaction) {
        this.userId = userId;
        this.postId = postId;
        this.imageURL = reaction;
    }

    public Reaction(String userID, String postID) {
        this.userId = userID;
        this.postId = postID;
        this.imageURL = "";
    }


    public String getUserID() {
        return userId;
    }

    public void setUserID(String userID) {
        this.userId = userID;
    }

    public String getPostID() {
        return postId;
    }

    public void setPostID(String postID) {
        this.postId = postID;
    }

    public String getReaction() {
        return imageURL;
    }

    public void setReaction(String reaction) {
        this.imageURL = reaction;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
