package com.example.myapplication.dataBase;


import android.graphics.Bitmap;

public class InfoUser {
    private int id;
    private String username;
    private Bitmap image1 = null;
    private String password;

    public InfoUser() {

    }

    public InfoUser(String username, Bitmap image1, String password) {
        this.username   =   username;
        this.image1     =   image1;
        this.password   =   password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getImage1() {
        return image1;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
