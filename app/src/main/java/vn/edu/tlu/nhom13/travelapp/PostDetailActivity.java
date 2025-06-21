package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {

    TextView txtTitle, txtRegion, txtDescription, txtStatus;
    ImageView imgDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        txtTitle = findViewById(R.id.txtDetailTitle);
        txtRegion = findViewById(R.id.txtDetailRegion);
        txtDescription = findViewById(R.id.txtDetailDescription);
        imgDetail = findViewById(R.id.imgDetail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String region = intent.getStringExtra("region");
        String description = intent.getStringExtra("description");
        String imagePath = intent.getStringExtra("imagePath");


        txtTitle.setText(title);
        txtRegion.setText("Khu vực: " + region);
        txtDescription.setText(description);

        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(this).load(imagePath).into(imgDetail);
        } else {
            imgDetail.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
