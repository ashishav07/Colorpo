package com.example.colorpo;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private TextView mobile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView posts;
    private Intent intent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment,container,false);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView email = root.findViewById(R.id.email);
        TextView username = root.findViewById(R.id.username);
        intent = new Intent(getActivity(),EditProfileActivity.class);
        username.setText(mUser.getDisplayName());
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
}
