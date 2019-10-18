package com.example.colorpo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Edit Profile</font>"));        }
    }
}
