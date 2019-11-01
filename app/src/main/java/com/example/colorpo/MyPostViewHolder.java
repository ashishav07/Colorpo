package com.example.colorpo;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyPostViewHolder extends RecyclerView.ViewHolder {
    public ImageView userImage;
    private final Context context;
    public TextView likes,username,subject,cdesc,notPosted;
    public Button like,contact,del;
    public MyPostViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        userImage = itemView.findViewById(R.id.user_image);
        subject = itemView.findViewById(R.id.subject);
        like = itemView.findViewById(R.id.like);
        contact = itemView.findViewById(R.id.contact);
        cdesc = itemView.findViewById(R.id.content_desc);
        likes = itemView.findViewById(R.id.likes);
        del = itemView.findViewById(R.id.delete);
        notPosted = itemView.findViewById(R.id.notPosted);
    }

}
