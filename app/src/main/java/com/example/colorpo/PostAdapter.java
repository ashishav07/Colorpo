package com.example.colorpo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    ArrayList<Post> postArrayList;

    public PostAdapter(Context context,ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_post_item,parent,false);
        return new PostViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.subject.setText(postArrayList.get(position).getSubject());
        holder.description.setText(postArrayList.get(position).getDescription());
        holder.time.setText(postArrayList.get(position).getTime());
        holder.username.setText(postArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
    public int getDatabaseCount(){
        final int[] count = new int[1];
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            count[0] = task.getResult().size();
                        }
                    }
                });
        return count[0];
    }
}
