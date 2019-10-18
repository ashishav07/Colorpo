package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        findViewById(R.id.forgot).setOnClickListener(this);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Reset your password</font>"));        }
    }

    @Override
    public void onClick(View view) {
        EditText et = findViewById(R.id.email);
        String str = et.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(str)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Reset Link Sent",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotActivity.this,LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
