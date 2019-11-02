package com.example.colorpo;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
public class MyPostsFragment extends Fragment{
    private ArrayList<Post> postArrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference ref = storage.getReference();
    private ViewGroup root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_my_posts,container,false);
        recyclerView = new RecyclerView(getContext());
        recyclerView = root.findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postArrayList = new ArrayList<>();
        loadDataFromDatabase();
        return root;
    }

    public void loadDataFromDatabase(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading your timeline...");
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
                                    querySnapshot.getString("timestamp"),
                                    querySnapshot.getString("dp"),
                                    "",
                                    querySnapshot.getString("Pid")
                            );
                            String id = null;
                            String userId = null;
                            id = querySnapshot.getString("id");
                            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            while(id == null || userId == null){
                                id = querySnapshot.getString("Pid");
                                id = id.trim();
                                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                userId = userId.trim();
                            }
                            if(id.equals(userId)) {
                                postArrayList.add(post);
                            }
                        }
                        if(postArrayList.isEmpty()){
                            root.findViewById(R.id.notPosted).setVisibility(View.VISIBLE);
                        }
                        else {
                            MyPostAdapter postAdapter = new MyPostAdapter(getActivity(), postArrayList);
                            recyclerView.setAdapter(postAdapter);
                        }
                        progressDialog.hide();
                    }
                });
    }
}