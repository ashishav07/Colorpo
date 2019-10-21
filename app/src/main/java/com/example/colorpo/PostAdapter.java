package com.example.colorpo;

import android.content.Context;
import com.bumptech.glide.Glide;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.subject.setText(postArrayList.get(position).getSubject());
        holder.description.setText(postArrayList.get(position).getDescription());
        holder.time.setText(postArrayList.get(position).getTime());
        holder.username.setText(postArrayList.get(position).getName());

        String st = "<b>"+postArrayList.get(position).getName()+"</b>"+" updated a post on "+postArrayList.get(position).getTime();
        holder.cdesc.setText(Html.fromHtml(st));
        //Glide.with(context)
          //      .load(postArrayList.get(position).getDp()).into(holder.userImage);
        Picasso.get().load(postArrayList.get(position).getDp()).placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
}
