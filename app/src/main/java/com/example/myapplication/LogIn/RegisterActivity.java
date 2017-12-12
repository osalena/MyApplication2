package com.example.myapplication.LogIn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    /*
    declare flag for show/hide password
     */
    boolean VISIBLE_PASSWORD = false;

    Button loginPassword = null;
    Button signIn = null;
    EditText password = null;
    EditText input_username = null;
    EditText input_password = null;
    EditText input_email = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*mapping*/
        loginPassword   =   (Button)findViewById(R.id.showPassword_button);
        signIn          =   (Button)findViewById(R.id.createAcc_button);
        password        =   (EditText)findViewById(R.id.input_password);
        input_username  =   (EditText)findViewById(R.id.input_username);
        input_password  =   (EditText)findViewById(R.id.input_password);
        input_email     =   (EditText)findViewById(R.id.input_email);
        /*set up listener*/
        loginPassword.setOnClickListener(onClickListenerPassword);
        signIn.setOnClickListener(onClickListenerCreateAcc);
    }

    /*
    onClick listener for show/hide button
     */
    android.view.View.OnClickListener onClickListenerPassword = new View.OnClickListener() {
        public void onClick(View v) {
            if (VISIBLE_PASSWORD) {
                VISIBLE_PASSWORD = false;
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                loginPassword.setText(R.string.reg_showPassword);
            } else {
                VISIBLE_PASSWORD = true;
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                loginPassword.setText(R.string.reg_hidePassword);
            }
        }
    };

    /*
    onClick listener for Create a new account button
     */
    android.view.View.OnClickListener onClickListenerCreateAcc = new View.OnClickListener() {
        public void onClick(View v) {
            if(isEmptyInput(input_email) || isEmptyInput(input_password) || isEmptyInput(input_username)) {
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


        }
    };

    public Context getActivity() {
        return RegisterActivity.this;
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
}
