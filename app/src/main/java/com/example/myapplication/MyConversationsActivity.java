package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

public class MyConversationsActivity extends AppCompatActivity {
    private InfoUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conversations);

        //to open DB
        MyInfoManager.getInstance().openDataBase(this);

        /* get current user info */
        Bundle b = getIntent().getExtras();
        curUser= MyInfoManager.getInstance().readUser(String.valueOf(b.getInt("user")));

        /* to display BACK button */
        android.app.ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Conversations");
    }

    /* Listener for menu's items*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                super.onBackPressed();
                return true;
        }
        return true;
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
