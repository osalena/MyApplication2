package com.example.myapplication.dataBase;


import android.graphics.Bitmap;

public class InfoReceipt {
    private int id;
    private String title;
    private String description;
    private Bitmap image1 = null;
    private String date = null;
    private String category = null;
    private int userId = -1;

    public InfoReceipt() {

    }

    public InfoReceipt(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public InfoReceipt(String title, String description, Bitmap image1) {
        this.title = title;
        this.description = description;
        this.image1 = image1;
    }

    public InfoReceipt(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public InfoReceipt(String title, String description, Bitmap image1, String category) {
        this.title = title;
        this.description = description;
        this.image1 = image1;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage1() {
        return image1;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }


    public void setUserId(int userId) {
        this.userId  = userId;

    }

    public int getUserId() {
        return userId;
    }
}
