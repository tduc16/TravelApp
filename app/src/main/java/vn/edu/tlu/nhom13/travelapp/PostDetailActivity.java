package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
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




import com.bumptech.glide.Glide;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    TextView txtTitle, txtRegion, txtDescription;
    ImageView imgDetail;

    RecyclerView recyclerComments;
    EditText edtComment;
    Button btnSendComment;

    DatabaseHelper dbHelper;
    CommentAdapter commentAdapter;

    int postId = 0;  // đổi thành int
    int userId = 0;  // đổi thành int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Lấy dữ liệu Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String region = intent.getStringExtra("region");
        String description = intent.getStringExtra("description");
        String imagePath = intent.getStringExtra("imagePath");

        // Lấy postId và userId kiểu String, chuyển sang int
        try {
            postId = Integer.parseInt(intent.getStringExtra("postId"));
        } catch (NumberFormatException e) {
            postId = 0; // hoặc xử lý lỗi phù hợp
        }
        try {
            userId = Integer.parseInt(intent.getStringExtra("userId"));
        } catch (NumberFormatException e) {
            userId = 0; // hoặc xử lý lỗi phù hợp
        }

        // Khởi tạo views
        txtTitle = findViewById(R.id.txtDetailTitle);
        txtRegion = findViewById(R.id.txtDetailRegion);
        txtDescription = findViewById(R.id.txtDetailDescription);
        imgDetail = findViewById(R.id.imgDetail);

        recyclerComments = findViewById(R.id.recyclerComments);
        edtComment = findViewById(R.id.edtComment);
        btnSendComment = findViewById(R.id.btnSendComment);

        // Set dữ liệu lên view
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

        // Khởi tạo database helper
        dbHelper = new DatabaseHelper(this);

        // Lấy danh sách bình luận cho bài viết
        List<String> comments = dbHelper.getCommentsForPost(postId);

        // Setup adapter cho RecyclerView bình luận
        commentAdapter = new CommentAdapter(comments);
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerComments.setAdapter(commentAdapter);

        // Xử lý nút gửi bình luận
        btnSendComment.setOnClickListener(v -> {
            String newComment = edtComment.getText().toString().trim();
            if (!newComment.isEmpty()) {
                boolean success = dbHelper.addComment(postId, userId, newComment);
                if (success) {
                    comments.add(0, newComment);  // Thêm lên đầu danh sách
                    commentAdapter.notifyItemInserted(0);
                    recyclerComments.scrollToPosition(0);
                    edtComment.setText("");
                } else {
                    Toast.makeText(PostDetailActivity.this, "Lỗi khi gửi bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
