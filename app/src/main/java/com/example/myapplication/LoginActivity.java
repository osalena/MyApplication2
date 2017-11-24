package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button singUp;
    private TextView mLink;
    private TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        singUp = (Button) findViewById(R.id.email_button);
        singUp.setOnClickListener(onClickListener);
        mLink = (TextView) findViewById(R.id.continue_policy);
        if (mLink != null) {
            mLink.setMovementMethod(LinkMovementMethod.getInstance());
        }
        signin = (TextView) findViewById(R.id.signin_textView);
        signin.setOnClickListener(onClickListener);
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
                    LoginActivity.this.startActivity(intent2);
                    break;

            }

        }
    };
}
