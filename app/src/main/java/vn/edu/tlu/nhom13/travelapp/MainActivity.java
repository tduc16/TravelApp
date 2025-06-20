package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.adapter.PostAdapter;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Button btnApprovePosts;
    EditText edtSearch;

    String role = "user";
    int userId = -1;
    String username = "";

    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        btnApprovePosts = findViewById(R.id.btnApprovePosts);
        edtSearch = findViewById(R.id.edtSearch); // cần có trong layout

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        username = intent.getStringExtra("username");
        userId = intent.getIntExtra("userId", -1);

        if (role == null) role = "user";
        if (username == null) username = "";

        if (userId == -1) {
            Toast.makeText(this, "Lỗi khi nhận userId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toast.makeText(this, "Xin chào " + username + " (" + role + ")", Toast.LENGTH_SHORT).show();

        // Hiện nút duyệt bài nếu là admin
        if (role.equals("admin")) {
            btnApprovePosts.setVisibility(View.VISIBLE);
        } else {
            btnApprovePosts.setVisibility(View.GONE);
        }

        // Mở trang duyệt bài viết
        btnApprovePosts.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ApprovePostsActivity.class);
            startActivity(i);
        });

        // Khởi tạo database và adapter
        dbHelper = new DatabaseHelper(this);
        postList = dbHelper.getApprovedPosts();
        adapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(adapter);

        // Tìm kiếm tiêu đề
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });
    }
}

