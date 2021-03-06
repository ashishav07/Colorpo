package com.example.colorpo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName, lastName, phone;
    private String fname, lname, mobile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView imageView;
    private Uri filePath = null;
    private FirebaseUser mUser;
    private FirebaseStorage storage;
    private StorageReference storageReference, ref;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // firebase init
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        phone = findViewById(R.id.mobile);
        db.collection("Users").document(mUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firstName.setText(documentSnapshot.getString("fname"));
                        lastName.setText(documentSnapshot.getString("lname"));
                        phone.setText(documentSnapshot.getString("mobile"));
                    }
                });
        imageView = findViewById(R.id.user_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        final boolean[] flag = {true};
        ref = firebaseStorage.getReferenceFromUrl("gs://colorpo-6fb15.appspot.com/").child("images/" + mUser.getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                while (flag[0]) {
                    if (!uri.toString().isEmpty()) {
                        flag[0] = false;
                        Picasso.get().load(uri.toString()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(imageView);
                    }
                }
            }
        });
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = firstName.getText().toString();
                lname = lastName.getText().toString();
                mobile = phone.getText().toString();
                CollectionReference posts = db.collection("Posts");
                final Query query = posts.whereEqualTo("id", mUser.getUid());
                if (filePath != null) {
                    StorageReference reference = storageReference
                            .child("images/" + mUser.getUid());
                    reference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                    final String[] dp = {null};
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            while (dp[0] == null)
                                                dp[0] = uri.toString();
                                            updateData(query, dp[0]);
                                            Log.i("FilePath", String.valueOf(dp[0]));
                                            startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
                                        }
                                    });
                                }
                            });
                }
                Map<String, Object> data = new HashMap<>();
                data.put("fname", fname);
                data.put("lname", lname);
                data.put("mobile", mobile);
                db.collection("Users").document(mUser.getUid())
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fname + " " + lname).build();
                                mUser.updateProfile(profileUpdates);

                            }
                        });
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Edit Profile</font>"));
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                CircleTransform c = new CircleTransform();
                Bitmap bm = c.transform(bitmap);
                imageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateData(Query query, final String dp1) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    arrayList.add(documentSnapshot.getString("Pid"));
                }
                Log.i("ArrayList", String.valueOf(arrayList));
                WriteBatch batch = db.batch();
                for (int k = 0; k < arrayList.size(); k++) {
                    DocumentReference ref = db.collection("Posts").document(arrayList.get(k));
                    batch.update(ref, "dp", dp1);
                }
                batch.commit();
            }
        });
    }
}