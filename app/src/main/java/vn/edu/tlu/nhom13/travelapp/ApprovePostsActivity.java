package vn.edu.tlu.nhom13.travelapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.adapter.ApprovePostAdapter;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class ApprovePostsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ApprovePostAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_posts);

        recyclerView = findViewById(R.id.recyclerApprove);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        List<Post> pendingPosts = db.getPendingPosts();

        adapter = new ApprovePostAdapter(this, pendingPosts, db, this::loadPendingPosts);
        recyclerView.setAdapter(adapter);
    }

    private void loadPendingPosts() {
        List<Post> updatedPosts = db.getPendingPosts();
        adapter.setData(updatedPosts);
        adapter.notifyDataSetChanged();
    }
}
