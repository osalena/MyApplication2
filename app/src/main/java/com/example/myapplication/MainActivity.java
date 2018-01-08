package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Interface.LoadListContainer;
import com.example.myapplication.LogIn.LoginActivity;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private InfoUser user;

    TextView cont_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.deleteDatabase(MyInfoManager.getInstance().getDB().getDatabaseName());

        MyInfoManager.getInstance().openDataBase(this);

        //this.deleteDatabase(MyInfoManager.getInstance().getDB().getDatabaseName());

        //MyInfoManager.getInstance().createTable("t");

        //db.execSQL("DROP TABLE IF EXISTS tablename");
        //MyInfoManager.getInstance().openDataBase(this);

        setContentView(R.layout.activity_main);
        cont_textView=(TextView)findViewById(R.id.continue_textView);
        cont_textView.setOnClickListener(onClickListener);

        user = new InfoUser();

       Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.heart);
       user.setImage1(icon);
       user.setUsername("TESTUser");
        MyInfoManager.getInstance().createUser(user);

        /* test user */
        List<InfoUser> users = MyInfoManager.getInstance().getAllUsers();
        user = users.get(0);

        //if(user.getImage1() != null){
         //   Toast.makeText(MainActivity.this, "not null", Toast.LENGTH_SHORT).show();
        //}

        LoadListContainer.setCtx(this);

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




    /* ON CLICK listener
    *  opens next Activity
    *  --> LogIn Activity*/
    android.view.View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                /* transfer curent user to next activity */
                intent.putExtra("user", user.getId());
                MainActivity.this.startActivity(intent);

            }
        };



    }
