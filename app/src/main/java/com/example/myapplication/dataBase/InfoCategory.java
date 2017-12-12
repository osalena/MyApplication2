package com.example.myapplication.dataBase;


import android.graphics.Bitmap;

public class InfoCategory {
    private int id;
    private String title;
    private Bitmap image = null;

    public InfoCategory() {

    }

    public InfoCategory(String title, Bitmap image) {
        this.title   =   title;
        this.image     =   image;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
