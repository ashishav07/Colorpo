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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
public class LikedPostsFragment extends Fragment{
    private final ArrayList<Post> postArrayList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ViewGroup root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);
        root = (ViewGroup) inflater.inflate(R.layout.fragment_liked_posts,container,false);
        recyclerView = new RecyclerView(getContext());
        recyclerView = root.findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDataFromDatabase();
        return root;
    }

    public void loadDataFromDatabase(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading your posts...");
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
                            final Post post = new Post(
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
                            String pId = querySnapshot.getString("Pid");
                            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            while(pId == null){
                                pId = querySnapshot.getString("Pid");
                                uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                uId = uId.trim();
                            }
                            final DocumentReference documentReference = db.collection("Likes").
                                    document(pId.trim()).
                                    collection("User").document(uId.trim());
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().exists()){
                                            postArrayList.add(post);
                                            PostAdapter postAdapter = new PostAdapter(getActivity(), postArrayList);
                                            recyclerView.setAdapter(postAdapter);
                                            root.findViewById(R.id.notPosted).setVisibility(View.GONE);
                                            progressDialog.hide();
                                        }
                                        else{
                                            root.findViewById(R.id.notPosted).setVisibility(View.VISIBLE);
                                            progressDialog.hide();
                                        }
                                    }
                                }
                            });
                        }

                    }
                });
    }
}