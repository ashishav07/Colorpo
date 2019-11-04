package com.example.colorpo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DescribedPostActivity extends AppCompatActivity {
    private TextView desc,subject,cdesc;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_described_post);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>View Post</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        desc = findViewById(R.id.description);
        desc.setText(intent.getStringExtra("description"));
        email = intent.getStringExtra("email");
        subject = findViewById(R.id.subject);
        subject.setText(intent.getStringExtra("subject"));
        cdesc = findViewById(R.id.content_desc);
        String descript = intent.getStringExtra("username") + " posted this on " + intent.getStringExtra("time");
        cdesc.setText(descript);
        findViewById(R.id.contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email));
                intent.putExtra(Intent.EXTRA_SUBJECT,subject.getText());
                startActivity(Intent.createChooser(intent,"Email to.."));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
