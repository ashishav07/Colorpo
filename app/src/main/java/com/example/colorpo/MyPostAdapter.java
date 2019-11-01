package com.example.colorpo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Map;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private ArrayList<Post> postArrayList;
    public MyPostAdapter(Context context,ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }
    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_post_layout,parent,false);
        return new MyPostViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyPostViewHolder holder, final int position) {
        holder.subject.setText(postArrayList.get(position).getSubject());
        final String postId = postArrayList.get(position).getPid();
        holder.likes.setText(postArrayList.get(position).getLikes());
        String st = "<b>" + postArrayList.get(position).getName() + "</b>" + " updated a post on " + postArrayList.get(position).getTime();
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
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        holder.like.setText("Unlike");
                    } else {
                        holder.like.setText("Like");
                    }
                }
            }
        });
        holder.cdesc.setText(Html.fromHtml(st));

        // like button click listener
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getText().toString() == "Like") {
                    Map<String, Object> map = new HashMap<>();
                    map.put("liked", 1);
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
                            if (task.isSuccessful()) {
                                holder.like.setText("Unlike");
                            }
                        }
                    });
                } else {
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
                            if (task.isSuccessful()) {
                                holder.like.setText("Like");
                            }
                        }
                    });
                }
            }
        });
        //like button listener ends

        //delete button on click listener

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setTitle("Delete");
                myAlert.setMessage("Delete the post ?");
                myAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Likes").document(postId)
                                .collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
                        db.collection("Posts").document(postId).delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Intent intent = new Intent(context,HomeActivity.class);
                                                            context.startActivity(intent);
                                                            Toast.makeText(context,"Post deleted",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                    }
                });
                myAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
            }
        });
        // delete button on click listener ends

        //contact on click listener
        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = postArrayList.get(position).getEmail();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email));
                intent.putExtra(Intent.EXTRA_SUBJECT, postArrayList.get(position).getEmail());
                context.startActivity(Intent.createChooser(intent, "Email to.."));
            }
        });
        //contact on click listener ends

        //image setter
        Picasso.get().load(postArrayList.get(position).getDp()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(holder.userImage);
        //image setter ends
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DescribedPostActivity.class);
                intent.putExtra("username", postArrayList.get(position).getName());
                intent.putExtra("description", postArrayList.get(position).getDescription());
                intent.putExtra("time", postArrayList.get(position).getTime());
                intent.putExtra("subject", postArrayList.get(position).getSubject());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
}