package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {
    private TextInputEditText subject;
    private EditText desc;
    private TextInputLayout subject_layout;
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postContent();
            }
        });
        desc = findViewById(R.id.des);
        subject = findViewById(R.id.sub);
        subject_layout = findViewById(R.id.subject_layout);

    }
    private void postContent() {
        if (subject.getText().toString().trim().equals("")) {
            subject_layout.setError("Subject is mandatory!");
        }
        if (desc.getText().toString().trim().equals("")) {
            desc.setError("Description is mandatory!");
        }
        post = new Post(subject.getText().toString().trim(), desc.getText().toString().trim());
        FirebaseDatabase.getInstance().getReference("Posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    Toast.makeText(getApplicationContext(),"Posted", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
    }
}