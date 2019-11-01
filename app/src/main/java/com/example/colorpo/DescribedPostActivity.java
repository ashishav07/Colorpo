package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DescribedPostActivity extends AppCompatActivity {
    private TextView desc,subject,cdesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_described_post);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>View Post</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        desc = findViewById(R.id.description);
        desc.setText(intent.getStringExtra("description"));
        subject = findViewById(R.id.subject);
        subject.setText(intent.getStringExtra("subject"));
        cdesc = findViewById(R.id.content_desc);
        String descript = intent.getStringExtra("username") + " posted this on " + intent.getStringExtra("time");
        cdesc.setText(descript);
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
