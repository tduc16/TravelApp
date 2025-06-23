package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.adapter.CommentAdapter;
import vn.edu.tlu.nhom13.travelapp.models.Comment;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity {

    TextView txtTitle, txtRegion, txtDescription;
    ImageView imgDetail;

    RecyclerView recyclerComments;
    EditText edtComment;
    Button btnSendComment;

    DatabaseHelper dbHelper;
    CommentAdapter commentAdapter;
    List<Comment> comments;

    int postId = 0;
    int userId = 0;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Ánh xạ view
        txtTitle = findViewById(R.id.txtDetailTitle);
        txtRegion = findViewById(R.id.txtDetailRegion);
        txtDescription = findViewById(R.id.txtDetailDescription);
        imgDetail = findViewById(R.id.imgDetail);

        recyclerComments = findViewById(R.id.recyclerComments);
        edtComment = findViewById(R.id.edtComment);
        btnSendComment = findViewById(R.id.btnSendComment);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String region = intent.getStringExtra("region");
        String description = intent.getStringExtra("description");
        String imagePath = intent.getStringExtra("imagePath");

        try {
            postId = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("postId")));
        } catch (NumberFormatException e) {
            postId = 0;
        }

        // Lấy username từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = prefs.getString("username", "");
        dbHelper = new DatabaseHelper(this);
        userId = dbHelper.getUserId(username);

        // Gán dữ liệu bài viết
        txtTitle.setText(title != null ? title : "Không có tiêu đề");
        txtRegion.setText("Khu vực: " + (region != null ? region : "Chưa xác định"));
        txtDescription.setText(description != null ? description : "Không có mô tả");

        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(this).load(imagePath).into(imgDetail);
            imgDetail.setContentDescription("Ảnh bài viết");
        } else {
            imgDetail.setImageResource(R.drawable.ic_launcher_background);
            imgDetail.setContentDescription("Ảnh mặc định");
        }

        // Load bình luận ban đầu
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        loadComments();

        // Xử lý khi gửi bình luận
        btnSendComment.setOnClickListener(v -> {
            String newComment = edtComment.getText().toString().trim();
            if (!newComment.isEmpty()) {
                boolean success = dbHelper.addComment(postId, userId, username, newComment);
                if (success) {
                    edtComment.setText("");
                    loadComments(); // reload lại danh sách bình luận
                    recyclerComments.scrollToPosition(0); // cuộn lên đầu
                } else {
                    Toast.makeText(PostDetailActivity.this, "Lỗi khi gửi bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadComments() {
        comments = dbHelper.getCommentsForPost(postId);
        commentAdapter = new CommentAdapter(comments);
        recyclerComments.setAdapter(commentAdapter);
    }
}
