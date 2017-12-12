package com.example.myapplication.LogIn;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

public class LoginActivity extends AppCompatActivity {

    private Button      singUp;
    private TextView    mLink;
    private TextView    signin;
    private InfoUser    curUser;
    private Button      facebookSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        singUp          = (Button) findViewById(R.id.email_button);
        facebookSingUp  = (Button)findViewById(R.id.button_facebook);
        mLink           = (TextView) findViewById(R.id.continue_policy);
        signin          = (TextView) findViewById(R.id.signin_textView);
        if (mLink != null) {
            mLink.setMovementMethod(LinkMovementMethod.getInstance());
        }

        signin.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        facebookSingUp.setOnClickListener(onClickListener);
        signin.setOnClickListener(onClickListener);
        singUp.setOnClickListener(onClickListener);
        /* open data base */
        MyInfoManager.getInstance().openDataBase(this);

        /* get current user info */
        Bundle b = getIntent().getExtras();
        curUser= MyInfoManager.getInstance().readUser(b.getInt("user"));
        //Toast.makeText(LoginActivity.this, curUser.getUsername(), Toast.LENGTH_SHORT).show();
    }

    android.view.View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.email_button:
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(intent);
                    break;
                case R.id.signin_textView:
                    Intent intent2 = new Intent(LoginActivity.this, activity_signIn.class);
                    intent2.putExtra("user", curUser.getId());
                    LoginActivity.this.startActivity(intent2);
                    break;
                case R.id.button_facebook:
                    Intent i = new Intent(LoginActivity.this, activity_facebook_login.class);
                    LoginActivity.this.startActivity(i);
                    break;
            }

        }
    };

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
