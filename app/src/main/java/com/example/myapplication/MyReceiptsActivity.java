package com.example.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myapplication.ReceiptsList.ReceiptsListFragment;
import com.example.myapplication.dataBase.MyInfoManager;

public class MyReceiptsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_receipts);
        FragmentManager fm = getFragmentManager();
        MyReceiptsFragment fragment= new MyReceiptsFragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.my_receipts_layout, fragment, "fragment");
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    protected void onResume() {
        MyInfoManager.getInstance().openDataBase(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        MyInfoManager.getInstance().closeDataBase();
        super.onPause();
    }
}
