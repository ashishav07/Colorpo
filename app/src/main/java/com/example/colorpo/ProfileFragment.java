package com.example.colorpo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileFragment extends Fragment {
    private TextView mobile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView posts;
    private ImageView edit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment,container,false);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView email = root.findViewById(R.id.email);
        TextView username = root.findViewById(R.id.username);
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

        // Edit Profile Button
        edit = root.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfile.class);
                startActivity(i);
            }
        });

        return root;
    }
}
