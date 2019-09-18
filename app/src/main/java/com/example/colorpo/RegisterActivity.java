package com.example.colorpo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fname;
    private EditText lname;
    private EditText mobile;
    private EditText pass;
    private EditText pass1;
    private EditText email;
    private Button reg;
    private User user;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        reg = findViewById(R.id.reg);
        pass = findViewById(R.id.pass1);
        pass1 = findViewById(R.id.pass2);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        reg.setOnClickListener(this);
        reference = FirebaseDatabase.getInstance().getReference("Posts");

    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPassword(String pass,String pass1) {
        if (pass != null && pass.length() >= 6 && pass1.equals(pass)) {
            return true;
        }
        return false;
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
        if(!isValidName(fname.getText().toString().trim())){
            fname.setError("Invalid Name");
            return;
        }
        if(!isValidLName(lname.getText().toString().trim())){
            lname.setError("Invalid Name");
            return;
        }

        if(!isValidEmail(email.getText().toString().trim())){
            email.setError("Enter Valid Email");
            return;
        }
        if(!isValidMobile(mobile.getText().toString().trim())){
            mobile.setError("Invalid Number");
            return;
        }
        if(!isValidPassword(pass.getText().toString(),pass1.getText().toString())){
            pass.setError("Invalid or Passwords do not match");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Long phn = Long.parseLong(mobile.getText().toString().trim());
                        if (task.isSuccessful()) {
                            fUser = mAuth.getCurrentUser();
                            user = new User(
                                    fname.getText().toString().trim(),
                                    lname.getText().toString().trim(),
                                    phn,
                                    email.getText().toString().trim()
                            );
                                    FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(),"Email Verification Sent",Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "failure" , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            email.setError("Email Already in use");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}