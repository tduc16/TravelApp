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
import vn.edu.tlu.nhom13.travelapp.ApprovePostsActivity;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button btnApprovePosts;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;

    private String role = "user";
    private int userId = -1;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getUserInfoFromIntent();

        if (userId == -1) {
            Toast.makeText(this, "Lỗi khi nhận userId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toast.makeText(this, "Xin chào " + username + " (" + role + ")", Toast.LENGTH_SHORT).show();
        btnApprovePosts.setVisibility(role.equals("admin") ? View.VISIBLE : View.GONE);

        btnApprovePosts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ApprovePostsActivity.class);
            startActivity(intent);
        });

        setupRecyclerView();
        setupSearchFilter();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        btnApprovePosts = findViewById(R.id.btnApprovePosts);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getUserInfoFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            role = intent.getStringExtra("role");
            username = intent.getStringExtra("username");
            userId = intent.getIntExtra("userId", -1);

            if (role == null) role = "user";
            if (username == null) username = "";
        }
    }

    private void setupRecyclerView() {
        dbHelper = new DatabaseHelper(this);
        postList = dbHelper.getApprovedPosts();

        adapter = new PostAdapter(this, postList, userId, post -> {
            // Khi nhấn nút chỉnh sửa, chuyển sang EditPostActivity
            Intent intent = new Intent(MainActivity.this, EditPostActivity.class);
            intent.putExtra("postId", post.getId());
            startActivity(intent);
        }, post -> {
            // Khi nhấn nút "Thêm", chuyển sang AddPostActivity (thêm bài viết mới)
            Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
            // Có thể truyền userId nếu cần
            intent.putExtra("postId", userId);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void setupSearchFilter() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        reloadPostList(); // Load lại danh sách bài viết khi quay lại activity
    }

    private void reloadPostList() {
        postList = dbHelper.getApprovedPosts();
        adapter.setData(postList);
        adapter.notifyDataSetChanged();
    }

}