package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    private TextInputEditText subject;
    private EditText desc;
    private TextInputLayout subject_layout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        subject_layout.setError(null);
        desc.setError(null);
        if (subject.getText().toString().trim().equals("")) {
            subject_layout.setError("Subject is mandatory!");
            return;
        }
        if (desc.getText().toString().trim().equals("")) {
            desc.setError("Description is mandatory!");
            return;
        }
        Map<String,Object> post = new HashMap<>();
        post.put("subject",subject.getText().toString().trim());
        post.put("description",desc.getText().toString().trim());
        db.collection("Posts").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostActivity.this, "Post Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "Error in creating Post", Toast.LENGTH_SHORT).show();
                    }
                })
        ;
    }
}