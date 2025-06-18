package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button btnApprovePosts; // nút duyệt bài viết
    String role = "user";   // mặc định là user
    int userId = -1;        // ID người dùng
    String username = "";   // tên đăng nhập

    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        postList = dbHelper.getApprovedPosts(); // lấy bài đã duyệt

        adapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(adapter);
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ nút
        btnApprovePosts = findViewById(R.id.btnApprovePosts);

        // Nhận dữ liệu từ Intent (LoginActivity)
        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        username = intent.getStringExtra("username");
        userId = intent.getIntExtra("userId", -1);

        // Gán mặc định nếu thiếu
        if (role == null) role = "user";
        if (username == null) username = "";

        // Kiểm tra userId hợp lệ
        if (userId == -1) {
            Toast.makeText(this, "Lỗi khi nhận userId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông báo
        Toast.makeText(this, "Xin chào " + username + " (" + role + ")", Toast.LENGTH_SHORT).show();

        // Phân quyền
        if (role.equals("admin")) {
            btnApprovePosts.setVisibility(View.VISIBLE);
        } else {
            btnApprovePosts.setVisibility(View.GONE);
        }

        // Bắt sự kiện nếu admin nhấn "Duyệt bài"
        btnApprovePosts.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn duyệt bài...", Toast.LENGTH_SHORT).show();
            // TODO: mở AdminActivity hoặc DuyetBaiActivity
        });
    }
}
