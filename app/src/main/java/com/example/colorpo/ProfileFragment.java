package com.example.colorpo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private TextView mobile;
    private ImageView imageView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView posts;
    private Intent intent;
    private StorageReference ref;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
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
        final boolean[] flag = {true};
        ref = firebaseStorage.getReferenceFromUrl("gs://colorpo-6fb15.appspot.com/").child("images/" + mUser.getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                while (flag[0]) {
                    if(!uri.toString().isEmpty()){
                        flag[0] = false;
                        Picasso.get().load(uri.toString()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(imageView);
                    }
                }
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
}
