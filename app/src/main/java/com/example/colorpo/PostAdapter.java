package com.example.colorpo;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

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
        holder.description.setText(postArrayList.get(position).getDescription());
        holder.time.setText(postArrayList.get(position).getTime());
        holder.username.setText(postArrayList.get(position).getName());
        holder.id.setText(postArrayList.get(position).getId());
        holder.Pid.setText(postArrayList.get(position).getPid());
        final String postId = holder.Pid.getText().toString();
        holder.likes.setText(postArrayList.get(position).getLikes());
        final String pLikes = holder.likes.getText().toString();
        holder.description.setVisibility(View.GONE);
        holder.time.setVisibility(View.GONE);
        holder.username.setVisibility(View.GONE);
        String st = "<b>"+postArrayList.get(position).getName()+"</b>"+" updated a post on "+postArrayList.get(position).getTime();
        holder.cdesc.setText(Html.fromHtml(st));
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
