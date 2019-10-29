package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    private TextInputEditText subject;
    private EditText desc;
    private TextInputLayout subject_layout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseUser mUser;
    private String posts;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference ref;
    private String dp = null;
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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = firebaseStorage.getReferenceFromUrl("gs://colorpo-6fb15.appspot.com/").child("images/" + mUser.getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                while (dp ==null)
                    dp = uri.toString();
            }
        });
        subject_layout = findViewById(R.id.subject_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Create Post</font>"));
        progressDialog = new ProgressDialog(this);
        if(progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setPosts(String posts) {
        int post = Integer.parseInt(posts);
        post = post+1;
        posts = Integer.toString(post);
        db.collection("Users").document(mUser.getUid()).update("posts",posts);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        String currentDateandTime = sdf.format(new Date());

        progressDialog.setTitle("Posting");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        Map<String, Object> post = new HashMap<>();
        post.put("subject", subject.getText().toString().trim());
        post.put("description", desc.getText().toString().trim());
        post.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        post.put("Name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        post.put("Likes","0");
        post.put("timestamp", currentDateandTime);
        post.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        post.put("name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        post.put("dp",dp);
        db.collection("Users").document(mUser.getUid()).get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                posts = documentSnapshot.getString("posts");
            }
        });
        String id = db.collection("Posts").document().getId();
        post.put("Pid",id);
        db.collection("Posts").document(id).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setPosts(posts);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                progressDialog.hide();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "Error in creating Post", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                });
    }

}