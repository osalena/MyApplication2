package com.example.myapplication.Interface;

import java.util.List;

public interface LoadListener {

    void onPostLoad(List<LoadListContainer> list);
    void onPreUpdate();

}