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
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private ArrayList<Post> postArrayList;
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
        holder.subject.setText(postArrayList.get(position).getSubject());
        final String postId = postArrayList.get(position).getPid();
        holder.likes.setText(postArrayList.get(position).getLikes()+" likes");
        String st = "<b>"+postArrayList.get(position).getName()+"</b>"+" updated a post on "+postArrayList.get(position).getTime();
        db.collection("Likes").document(postId).
                collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String z = Integer.toString(queryDocumentSnapshots.size());
                holder.likes.setText(z + " likes");
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
                if(holder.like.getText().toString().equals("Like")){
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
                            holder.likes.setText(z + " likes");
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
                            holder.likes.setText(z + " likes");
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
                intent.putExtra(Intent.EXTRA_SUBJECT,postArrayList.get(position).getSubject());
                context.startActivity(Intent.createChooser(intent,"Email to.."));
            }
        });
        // image loader
        Picasso.get().load(postArrayList.get(position).getDp()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(holder.userImage);
        // image loader ends

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DescribedPostActivity.class);
                intent.putExtra("username",postArrayList.get(position).getName());
                intent.putExtra("description",postArrayList.get(position).getDescription());
                intent.putExtra("time",postArrayList.get(position).getTime());
                intent.putExtra("subject",postArrayList.get(position).getSubject());
                intent.putExtra("email",postArrayList.get(position).getEmail());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
}