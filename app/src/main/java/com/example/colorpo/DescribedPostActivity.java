package com.example.colorpo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DescribedPostActivity extends AppCompatActivity {
private ImageView userimage;
    private StorageReference ref;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_described_post);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        userimage = findViewById(R.id.user_image);
        ref = FirebaseStorage.getInstance().getReference().child("images/"+ intent.getStringExtra("id"));
        final long ONE_MEGABYTE = 1024*1024;
        progressDialog.show();
        ref.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        CircleTransform tr = new CircleTransform();
                        Bitmap b = tr.transform(bm);
                        userimage.setImageBitmap(b);
                        progressDialog.hide();
                    }
                });
    }
}
