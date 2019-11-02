package com.example.colorpo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactFragment extends Fragment implements View.OnClickListener {
    private CardView c, c4, c5, c6, c7;
    private TextView e1, e2, e3, e4, e5;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        c = v.findViewById(R.id.cardView);
        c4 = v.findViewById(R.id.cardView4);
        c5 = v.findViewById(R.id.cardView5);
        c6 = v.findViewById(R.id.cardView6);
        c7 = v.findViewById(R.id.cardView7);
        e1 = v.findViewById(R.id.e1);
        e2 = v.findViewById(R.id.textView3);
        e3 = v.findViewById(R.id.textView5);
        e4 = v.findViewById(R.id.textView7);
        e5 = v.findViewById(R.id.textView9);
        c.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        c7.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cardView:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",e1.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.cardView4:
                Intent emailIntent2 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",e2.getText().toString(), null));
                emailIntent2.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent2.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent2, "Send email..."));
                break;
            case R.id.cardView5:
                Intent emailIntent3 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",e3.getText().toString(), null));
                emailIntent3.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent3.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent3, "Send email..."));
                break;
            case R.id.cardView6:
                Intent emailIntent4 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",e4.getText().toString(), null));
                emailIntent4.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent4.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent4, "Send email..."));
                break;
            case R.id.cardView7:
                Intent emailIntent5 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",e5.getText().toString(), null));
                emailIntent5.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent5.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent5, "Send email..."));
                break;
            default:
                break;
        }

    }

}
