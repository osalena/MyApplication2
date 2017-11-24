package com.example.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_main_cookpad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }



    public void showFragment1OnClick(View view) {

        // send data to fragment
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putInt("no", 123);
        args.putString("name", "Israel Israel");
        FirstFragment fragmet1= new FirstFragment();
        fragmet1.setArguments(args);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet1);
        t.addToBackStack(null);
        t.commit();

    }

    public void showFragment2OnClick(View view) {
        FragmentManager fm = getFragmentManager();
        AnimalsListFragment fragmet2= new AnimalsListFragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2);
        t.addToBackStack(null);
        t.commit();
    }


}
