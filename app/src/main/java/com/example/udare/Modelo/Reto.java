package com.example.udare.Modelo;

public class Reto {
    private String  title;
    private String descrition;
    private String category;

    public Reto(String title, String descrition, String category) {
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
