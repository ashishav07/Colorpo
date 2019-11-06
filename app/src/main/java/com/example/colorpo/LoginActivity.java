package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextInputEditText etEmail,etPassword;
    private TextInputLayout temail,tepassword;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.forgot).setOnClickListener(this);

        if(user!=null){
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
        }

        // Hide action bar
        if (getSupportActionBar()!=null)
        getSupportActionBar().hide();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPassword(String pass) {
        return pass.length() >= 6;
    }

    private void userLogin(){
        etEmail = findViewById(R.id.username);
        etPassword = findViewById(R.id.pass);
        temail = findViewById(R.id.username_layout);
        tepassword = findViewById(R.id.password_layout);
        temail.setError(null);
        tepassword.setError(null);
        String email,password;
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if(!isValidEmail(email)){
//            etEmail.setError("Enter Valid Email");
            temail.setError("E-Mail isn't valid");
            return;
        }
        else {
            temail.setError(null);
        }
        if(!isValidPassword(password)){
//            etPassword.setError("Invalid password!");
            tepassword.setError("Invalid Password");
            return;
        }
        else {
            temail.setError(null);
        }
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = mAuth.getCurrentUser();
                    if(user.isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email Not Verified",Toast.LENGTH_LONG).show();
                        progressDialog.hide();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
                etPassword.setText(null);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login:

                userLogin();
                break;
            case R.id.forgot :
                Intent forgot_intent = new Intent(this,ForgotActivity.class);
                startActivity(forgot_intent);
                break;
        }
    }
}
