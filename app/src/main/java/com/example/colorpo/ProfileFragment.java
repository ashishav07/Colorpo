package com.example.colorpo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {
    private TextView mobile;
    private ImageView imageView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView posts;
    private Intent intent;
    private StorageReference ref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment,container,false);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView email = root.findViewById(R.id.email);
        TextView username = root.findViewById(R.id.username);
        ref = FirebaseStorage.getInstance().getReference().child("images/" + mUser.getUid());
        intent = new Intent(getActivity(),EditProfileActivity.class);
        username.setText(mUser.getDisplayName());
        imageView = root.findViewById(R.id.user_image);
        posts = root.findViewById(R.id.posts);
        email.setText(mUser.getEmail());
        mobile = root.findViewById(R.id.mobile);
        String userid= mUser.getUid();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Your Profile...");
        progressDialog.show();
        db.document("Users/" + userid).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        mobile.setText(documentSnapshot.getString("mobile"));
                        posts.setText(documentSnapshot.getString("posts"));
                    }
                }
            });
        final long ONE_MEGABYTE = 1024*1024;
        ref.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                  //      Picasso.get().load(getImageUri(getActivity(), bm)).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(imageView);
                        CircleTransform tr = new CircleTransform();
                        Bitmap b = tr.transform(bm);
                        imageView.setImageBitmap(b);
                        progressDialog.hide();
                    }
                });
        root.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(intent);
            }
        });
        return root;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
