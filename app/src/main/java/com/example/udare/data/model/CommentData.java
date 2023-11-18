package com.example.udare.data.model;

import java.io.Serializable;

public class CommentData implements Serializable {
    public String userId;
    public String comment;

    public CommentData(String userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }
}