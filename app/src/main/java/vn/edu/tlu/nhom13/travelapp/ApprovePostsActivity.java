package vn.edu.tlu.nhom13.travelapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.adapter.ApprovePostAdapter;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class ApprovePostsActivity extends AppCompatActivity {

    RecyclerView recyclerApprove;
    DatabaseHelper db;
    ApprovePostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_posts);

        recyclerApprove = findViewById(R.id.recyclerApprove);
        recyclerApprove.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        List<Post> pendingPosts = db.getPendingPosts();

        adapter = new ApprovePostAdapter(this, pendingPosts, db);
        recyclerApprove.setAdapter(adapter);
    }
}