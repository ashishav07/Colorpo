package com.example.colorpo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fname;
    private EditText lname;
    private EditText username;
    private EditText mobile;
    private EditText pass;
    private EditText pass1;
    private EditText email;
    private Button reg;
    private Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        reg = findViewById(R.id.reg);
        pass = findViewById(R.id.pass1);
        pass1 = findViewById(R.id.pass2);


        reg.setOnClickListener(this);
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPassword(String pass,String pass1) {
        if (pass != null && pass.length() > 6 && pass1.equals(pass)) {
            return true;
        }
        return false;
    }

    private Boolean isValidUsername(String username){
        String User_Pattern = "^\\S+\\w{8,32}\\S{1,}";
        Pattern pattern = Pattern.compile(User_Pattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
    private Boolean isValidName(String name){
        String name_Pattern = "^[\\p{L}.'-]+$";
        Pattern pattern = Pattern.compile(name_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    private Boolean isValidLName(String name){
        String name_Pattern = "^[\\p{L}. '-]+$";
        Pattern pattern = Pattern.compile(name_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    private Boolean isValidMobile(String number){
        String num_Pattern = "^[1-9]{1}[0-9]{9}$";
        Pattern pattern = Pattern.compile(num_Pattern);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    @Override
    public void onClick(View view) {
        flag = TRUE;
        if(view == reg){
            if(isValidEmail(email.getText().toString())){
                Log.i("email", "Email Match");
            }
            else{
                email.setError("Enter Valid Email");
                flag = FALSE;
            }
            if(isValidPassword(pass.getText().toString(),pass1.getText().toString())){
                Log.i("pass", "Passwords Match");
            }
            else{
                pass.setError("Invalid or Passwords do not match");
                flag = FALSE;
            }
            if(isValidUsername(username.getText().toString())){
                Log.i("username", "Username Valid");
            }
            else{
                username.setError("Invalid Username");
                flag = FALSE;
            }
            if(isValidName(fname.getText().toString())){
                Log.i("fname", "First name Valid");
            }
            else{
                fname.setError("Invalid Name");
                flag = FALSE;
            }

            if(isValidLName(lname.getText().toString())){
                Log.i("lname", "last name Valid");
            }
            else{
                lname.setError("Invalid Name");
                flag = FALSE;
            }

            if(isValidMobile(mobile.getText().toString())){
                Log.i("mobile", "Number Valid");
            }
            else{
                lname.setError("Invalid Number");
                flag = FALSE;
            }

        }
    }
}
