package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName,lastName,phone;
    private String fname,lname,mobile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
            progressDialog = new ProgressDialog(this);
            firstName = findViewById(R.id.firstname);
            lastName = findViewById(R.id.lastname);
            phone = findViewById(R.id.mobile);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            firstName.setText(documentSnapshot.getString("fname"));
                            lastName.setText(documentSnapshot.getString("lname"));
                            phone.setText(documentSnapshot.getString("mobile"));
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            progressDialog.hide();
                        }
                    });

            findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.setMessage("Updating your Profile");
                    progressDialog.show();
                    fname = firstName.getText().toString();
                    lname = lastName.getText().toString();
                    mobile = phone.getText().toString();
                    Map<String,Object> data = new HashMap<>();
                    data.put("fname",fname);
                    data.put("lname",lname);
                    data.put("mobile",mobile);

                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fname + " " + lname).build();
                                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Data Updated",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                            }
                                        }
                                    });
                                    progressDialog.hide();
                                }
                            });

                }
            });
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Edit Profile</font>"));        }
        }

}
