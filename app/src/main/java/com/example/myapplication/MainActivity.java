package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

public class MainActivity extends AppCompatActivity {

    TextView cont_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyInfoManager.getInstance().openDataBase(this);

        setContentView(R.layout.activity_main);
        cont_textView=(TextView)findViewById(R.id.continue_textView);
        cont_textView.setOnClickListener(onClickListener);
        //Bitmap icon = BitmapFactory.decodeResource(this.getResources(),R.drawable.chess_pie);
        //InfoReceipt r1 = new InfoReceipt("DDDDD", "DDDDDDDDDDDDD", icon);
        //InfoUser us = new InfoUser("AA", null, "123");
       // MyInfoManager.getInstance().createReceipt(us, r1);
       // MyInfoManager.getInstance().createUser(us);
       // Toast.makeText(this, MyInfoManager.getInstance().getAllUsers().toString(), Toast.LENGTH_SHORT);

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





    android.view.View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);

                //if(MyInfoManager.getInstance().)
                //Toast.makeText(getApplicationContext(), "dfdf", Toast.LENGTH_SHORT).show();
              // Toast.makeText(getApplicationContext(), MyInfoManager.getInstance().readUser(1).getUsername(), Toast.LENGTH_SHORT).show();
            }
        };



    }
