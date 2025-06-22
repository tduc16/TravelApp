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

import vn.edu.tlu.nhom13.travelapp.adapter.ManagePostAdapter;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class ManagePostsActivity extends AppCompatActivity {

    RecyclerView recyclerManage;
    DatabaseHelper db;
    ManagePostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_posts);

        recyclerManage = findViewById(R.id.recyclerManage);
        recyclerManage.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        List<Post> approvedPosts = db.getApprovedPosts();

        adapter = new ManagePostAdapter(this, approvedPosts, db);
        recyclerManage.setAdapter(adapter);
    }
}
