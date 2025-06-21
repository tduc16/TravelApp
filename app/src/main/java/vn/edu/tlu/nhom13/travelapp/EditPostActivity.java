package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class EditPostActivity extends AppCompatActivity {

    EditText edtTitle, edtDescription;
    Spinner spnRegion;
    Button btnUpdate;
    ImageView imgPreview;

    String imagePath;
    int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        spnRegion = findViewById(R.id.spnRegion);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgPreview = findViewById(R.id.imgPreview);

        String[] regions = {"Bắc", "Trung", "Nam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        spnRegion.setAdapter(adapter);

        // Lấy postId từ Intent
        Intent intent = getIntent();
        postId = intent.getIntExtra("postId", -1);

        if (postId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy bài viết", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Truy vấn dữ liệu từ cơ sở dữ liệu
        DatabaseHelper db = new DatabaseHelper(this);
        Post post = db.getPostById(postId);

        if (post != null) {
            edtTitle.setText(post.getTitle());
            edtDescription.setText(post.getDescription());
            spnRegion.setSelection(adapter.getPosition(post.getRegion()));
            imagePath = post.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                int resId = getResources().getIdentifier(imagePath, "drawable", getPackageName());
                if (resId != 0) {
                    imgPreview.setImageResource(resId); // Sử dụng drawable
                } else {
                    imgPreview.setImageResource(android.R.color.darker_gray); // Ảnh mặc định
                }
            }
        } else {
            Toast.makeText(this, "Không tìm thấy bài viết", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnUpdate.setOnClickListener(v -> {
            String newTitle = edtTitle.getText().toString().trim();
            String newDesc = edtDescription.getText().toString().trim();
            String newRegion = spnRegion.getSelectedItem().toString();

            if (newTitle.isEmpty() || newDesc.isEmpty()) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.updatePost(postId, newTitle, newDesc, newRegion, imagePath);

            Toast.makeText(this, "Đã cập nhật bài viết", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

