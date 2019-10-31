package com.example.colorpo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private ArrayList<Post> postArrayList;
    private List<DocumentSnapshot> userList;
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
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        userList = new ArrayList<>();
        holder.subject.setText(postArrayList.get(position).getSubject());
        holder.description.setText(postArrayList.get(position).getDescription());
        holder.time.setText(postArrayList.get(position).getTime());
        holder.username.setText(postArrayList.get(position).getName());
        holder.id.setText(postArrayList.get(position).getId());
        holder.Pid.setText(postArrayList.get(position).getPid());
        final String postId = holder.Pid.getText().toString();
        holder.likes.setText(postArrayList.get(position).getLikes());
        holder.description.setVisibility(View.GONE);
        holder.time.setVisibility(View.GONE);
        holder.username.setVisibility(View.GONE);
        String st = "<b>"+postArrayList.get(position).getName()+"</b>"+" updated a post on "+postArrayList.get(position).getTime();
        db.collection("Likes").document(postId).
                collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String z = Integer.toString(queryDocumentSnapshots.size());
                holder.likes.setText(z);
            }
        });

        DocumentReference documentReference = db.collection("Likes").document(postId).
                collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        holder.like.setText("Unlike");
                    }
                    else {
                        holder.like.setText("Like");
                    }
                }
            }
        });

        holder.cdesc.setText(Html.fromHtml(st));

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.getText().toString() == "Like"){
                    Map<String,Object> map = new HashMap<>();
                    map.put("liked",1);
                    db.collection("Likes").document(postId)
                           .collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                           .set(map);
                    db.collection("Likes").document(postId).
                            collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String z = Integer.toString(queryDocumentSnapshots.size());
                            holder.likes.setText(z);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                holder.like.setText("Unlike");
                            }
                        }
                    });
                }
                else{
                    db.collection("Likes").document(postId)
                            .collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
                    db.collection("Likes").document(postId).
                            collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String z = Integer.toString(queryDocumentSnapshots.size());
                            holder.likes.setText(z);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                holder.like.setText("Like");
                            }
                        }
                    });
                }
            }
        });

        //contact on click

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = postArrayList.get(position).getEmail();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email));
                intent.putExtra(Intent.EXTRA_SUBJECT,postArrayList.get(position).getEmail());
                context.startActivity(Intent.createChooser(intent,"Email to.."));
            }
        });

        //Glide.with(context)
        //      .load(postArrayList.get(position).getDp()).into(holder.userImage);
        Picasso.get().load(postArrayList.get(position).getDp()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(holder.userImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DescribedPostActivity.class);
                intent.putExtra("username",holder.username.getText());
                intent.putExtra("description",holder.description.getText());
                intent.putExtra("time",holder.time.getText());
                intent.putExtra("subject",holder.subject.getText());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
}
