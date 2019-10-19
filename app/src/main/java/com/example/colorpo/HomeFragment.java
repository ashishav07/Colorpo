package com.example.colorpo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView t1;
    ArrayList<Post> postArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment,container,false);
        recyclerView = new RecyclerView(getContext());
        recyclerView = root.findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postArrayList = new ArrayList<>();
        loadDataFromDatabase();
        return root;
    }
    private void loadDataFromDatabase(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading your data...");
        progressDialog.show();
        if(postArrayList.size()>0){
            postArrayList.clear();
        }
            db.collection("Posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot querySnapshot: task.getResult()){
                                Post post = new Post(
                                        querySnapshot.getString("subject"),
                                        querySnapshot.getString("description"),
                                        querySnapshot.getString("Name"),
                                        querySnapshot.getString("Likes"),
                                        querySnapshot.getString("email"),
                                        querySnapshot.getString("id"),
                                        querySnapshot.getString("timestamp"));
                                postArrayList.add(post);
                            }
                            PostAdapter postAdapter = new PostAdapter(getActivity(),postArrayList);
                            recyclerView.setAdapter(postAdapter);
                            progressDialog.hide();
                        }
                    });

    }
}
