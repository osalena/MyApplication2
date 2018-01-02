package com.example.myapplication.LogIn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity_main_cookpad;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

import java.util.regex.Pattern;

public class activity_signIn extends AppCompatActivity {

    /*
      declare flag for show/hide password
       */
    boolean VISIBLE_PASSWORD = false;

    private Button      loginPassword;
    private Button      signIn;
    private EditText    input_username;
    private EditText    input_password;
    private InfoUser    curUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        /*mapping*/
        loginPassword   =   (Button)findViewById(R.id.showPassword_button);
        signIn          =   (Button)findViewById(R.id.createAcc_button);
        input_username  =   (EditText)findViewById(R.id.input_username2);
        input_password  =   (EditText)findViewById(R.id.input_password2);

        /*set up listener*/
        loginPassword.setOnClickListener(onClickListenerPassword);
        signIn.setOnClickListener(onClickListenerCreateAcc);

        MyInfoManager.getInstance().openDataBase(this);

        /* get current user info */
        Bundle b = getIntent().getExtras();
        curUser= MyInfoManager.getInstance().readUser(String.valueOf(b.getInt("user")));
        //Toast.makeText(activity_signIn.this, curUser.getUsername(), Toast.LENGTH_SHORT).show();

        input_username.setText(curUser.getUsername());
        input_password.setText(curUser.getPassword());
    }

    /*
    onClick listener for show/hide button
     */
    android.view.View.OnClickListener onClickListenerPassword = new View.OnClickListener() {
        public void onClick(View v) {
            if (VISIBLE_PASSWORD) {
                VISIBLE_PASSWORD = false;
                input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                loginPassword.setText(R.string.reg_showPassword);
            } else {
                VISIBLE_PASSWORD = true;
                input_password.setInputType(InputType.TYPE_CLASS_TEXT);
                loginPassword.setText(R.string.reg_hidePassword);
            }
        }
    };

    /*
    onClick listener for Create a new account button
     */
    android.view.View.OnClickListener onClickListenerCreateAcc = new View.OnClickListener() {
        public void onClick(View v) {
            /*if(isEmptyInput(input_email) || isEmptyInput(input_password) || isEmptyInput(input_username)) {
                Toast.makeText(getActivity(),R.string.reg_emptyToast,
                        Toast.LENGTH_LONG).show();
                return;
            }

            if(!checkEmail(input_email.getText().toString()) &&
                    input_password.getText().toString().trim().length()<6){
                Toast.makeText(getActivity(), getActivity().getString(R.string.reg_emailToast) + " and "
                                + getActivity().getString(R.string.reg_passwordToast),
                        Toast.LENGTH_LONG).show();
                return;
            }

            if(!checkEmail(input_email.getText().toString())){
                Toast.makeText(getActivity(),R.string.reg_emailToast,
                        Toast.LENGTH_LONG).show();
            }

            if(input_password.getText().toString().trim().length()<6){
                Toast.makeText(getActivity(),R.string.reg_passwordToast,
                        Toast.LENGTH_LONG).show();
            }

            else {
                Intent intent = new Intent(activity_signIn.this, activity_main_cookpad.class);
                activity_signIn.this.startActivity(intent);
            } */

            Intent intent = new Intent(activity_signIn.this, activity_main_cookpad.class);
            intent.putExtra("user", curUser.getId());
            activity_signIn.this.startActivity(intent);

        }
    };

    public Context getActivity() {
        return activity_signIn.this;
    }

    private boolean isEmptyInput (EditText t){
        if(TextUtils.isEmpty(t.getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isValidEmail(EditText target) {
        return !TextUtils.isEmpty(target.getText().toString())
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target.getText().toString()).matches();
    }


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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
