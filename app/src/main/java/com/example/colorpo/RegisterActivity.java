package com.example.colorpo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fname;
    private EditText lname;
    private EditText mobile;
    private EditText pass;
    private EditText pass1;
    private EditText email;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        Button reg = findViewById(R.id.reg);
        pass = findViewById(R.id.pass1);
        pass1 = findViewById(R.id.pass2);
        mAuth = FirebaseAuth.getInstance();
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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        long phn = Long.parseLong(mobile.getText().toString().trim());
                        if (task.isSuccessful()) {
                            fUser = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fname.getText().toString().trim() + " " + lname.getText().toString().trim()).build();
                            fUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("Register Activity ", "User profile updated.");
                                    }
                                }
                            });
                                    //Firestore
                                    String first = fname.getText().toString().trim();
                                    String last = lname.getText().toString().trim();
                                    String phone = Long.toString(phn);
                                    String eMail = email.getText().toString().trim();
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("fname",first);
                                    user.put("lname",last);
                                    user.put("mobile",phone);
                                    user.put("email",eMail);
                                    user.put("posts","0");
                                    db.collection("Users").document(fUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"Email Verification Sent",Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    });
                                    //
                                }
                        else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            email.setError("Email Already in use");
                            progressDialog.hide();
                        }
                    }
                });
    }
}