package com.example.udare.data.model;

public class Challenge {
    private String  title;
    private String descrition;
    private String category;

    public Challenge(String title, String descrition, String category) {
        this.title = title;
        this.descrition = descrition;
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
