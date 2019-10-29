package com.example.colorpo;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView userImage;
    private final Context context;
    public TextView likes,username,subject,description,time,cdesc,id,Pid;
    public Button like,contact;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        userImage = itemView.findViewById(R.id.user_image);
        username = itemView.findViewById(R.id.username);
        subject = itemView.findViewById(R.id.subject);
        description = itemView.findViewById(R.id.description);
        time = itemView.findViewById(R.id.time);
        like = itemView.findViewById(R.id.like);
        contact = itemView.findViewById(R.id.contact);
        cdesc = itemView.findViewById(R.id.content_desc);
        id = itemView.findViewById(R.id.id);
        likes = itemView.findViewById(R.id.likes);
        Pid = itemView.findViewById(R.id.Pid);
    }

}
