package com.example.myapplication.Category;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myapplication.MyReceipts.MyReceiptsFragment;
import com.example.myapplication.R;

public class Category_list_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        FragmentManager fm = getFragmentManager();
        CategoryListFragment fragment= new CategoryListFragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.categoty_list_layout, fragment, "fragment");
        t.addToBackStack(null);
        t.commit();
    }
}
