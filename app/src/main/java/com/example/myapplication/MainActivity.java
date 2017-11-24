package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView cont_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cont_textView=(TextView)findViewById(R.id.continue_textView);
        cont_textView.setOnClickListener(onClickListener);
    }






    android.view.View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
            }
        };



    }
