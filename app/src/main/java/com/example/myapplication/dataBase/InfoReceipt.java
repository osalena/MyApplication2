package com.example.myapplication.dataBase;


import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InfoReceipt {
    private String id;
    private String title;
    private String description;
    private Bitmap image = null;
    private String userId = null;
    private boolean imageExist = false;

    public InfoReceipt() {
        id = getGeneratedId();
    }


    private String getGeneratedId() {
        return "i_" + System.currentTimeMillis();
    }

    public InfoReceipt(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public InfoReceipt(String title, String description) {
        id = getGeneratedId();
        this.title = title;
        this.description = description;
    }

    public InfoReceipt(String title, String description, Bitmap image) {
        id = getGeneratedId();
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean fromJson(JSONObject iObj) {
        boolean res = false;
        try {
            setId(iObj.getString("id"));
            setTitle(iObj.getString("title"));
            setDescription(iObj.getString("description"));
            setImageExist(iObj.getBoolean("img"));
            res = true;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return res;

    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    public void setUserId(String userId) {
        this.userId = userId;

    }

    public boolean isImageExist() {
        return imageExist;
    }

    public void setImageExist(boolean imageExist) {
        this.imageExist = imageExist;
    }

    public String getUserId() {
        return userId;
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }



    public static List<InfoReceipt> parseJson(String content) {

        List<InfoReceipt> receipts = null;
        try {

            JSONTokener jsonTokener = new JSONTokener(content);

            JSONObject json = (JSONObject) jsonTokener.nextValue();

            receipts = new ArrayList<InfoReceipt>();

            JSONArray receiptsJsonArr = json.getJSONArray("receipts");

            for (int i = 0; i < receiptsJsonArr.length(); i++) {
                try {
                    JSONObject fObj = receiptsJsonArr.getJSONObject(i);
                    InfoReceipt r = new InfoReceipt();
                    if(r.fromJson(fObj)){
                        receipts.add(r);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return receipts;
    }
}

