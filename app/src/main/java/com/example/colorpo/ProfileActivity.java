package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView email,mobile,posts,username;
    private ImageView userImage;
    private StorageReference ref;
    ProgressDialog progressDialog;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Profile...");
        progressDialog.show();
        Intent intent = getIntent();
        String uId = intent.getStringExtra("uid");
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        posts = findViewById(R.id.posts);
        userImage = findViewById(R.id.user_image);
        final String[] fname = new String[1];
        ref = FirebaseStorage.getInstance().getReference().child("images/" + uId);
        db.document("Users/" + uId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("fname") + " " + documentSnapshot.getString("lname");
                        fname[0] = documentSnapshot.getString("fname");
                        mobile.setText(documentSnapshot.getString("mobile"));
                        posts.setText(documentSnapshot.getString("posts"));
                        email.setText(documentSnapshot.getString("email"));
                        username.setText(name);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + fname[0] +"'s Profile</font>"));
                        }
                    }
                });
        final boolean[] flag = {true};
        ref = firebaseStorage.getReferenceFromUrl("gs://colorpo-6fb15.appspot.com/").child("images/" + uId);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                while (flag[0]) {
                    if(!uri.toString().isEmpty()){
                        flag[0] = false;
                        Picasso.get().load(uri.toString()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(userImage);
                        progressDialog.hide();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
