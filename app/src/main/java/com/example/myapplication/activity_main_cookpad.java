package com.example.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;


public class activity_main_cookpad extends AppCompatActivity{

    private Button trending;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getCategories();
        trending    =   (Button)findViewById(R.id.btn2);
        trending.callOnClick();
        searchView = (SearchView)findViewById(R.id.search);
        //searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();


    }

    private void getCategories() {

        FragmentManager categoryManager = getFragmentManager();
        //Bundle argsCat = new Bundle();
        //argsCat.putInt("no", 555);
        //argsCat.putStringArray("category", getResources().getStringArray(R.array.categories));
        //argsCat.putString("name", "Israel Israel");
        CategoryFragment fragmentCat= new CategoryFragment();
       // fragmentCat.setArguments(argsCat);
        FragmentTransaction tCat= categoryManager.beginTransaction();
        tCat.add(R.id.category_layoutt, fragmentCat);
        tCat.addToBackStack(null);
        tCat.commit();

        FragmentManager fm = getFragmentManager();
        AnimalsListFragment fragmet2= new AnimalsListFragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2);
        t.addToBackStack(null);
        t.commit();
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
