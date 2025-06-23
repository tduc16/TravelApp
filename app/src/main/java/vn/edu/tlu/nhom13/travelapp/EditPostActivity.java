package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class EditPostActivity extends AppCompatActivity {

    EditText edtTitle, edtDescription;
    Spinner spnRegion;
    Button btnUpdate;

    int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        spnRegion = findViewById(R.id.spnRegion);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Thiết lập danh sách vùng miền
        String[] regions = {"Bắc", "Trung", "Nam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRegion.setAdapter(adapter);

        // Lấy postId từ Intent
        Intent intent = getIntent();
        postId = intent.getIntExtra("postId", -1);

        if (postId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy bài viết", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Truy vấn bài viết từ database
        DatabaseHelper db = new DatabaseHelper(this);
        Post post = db.getPostById(postId);

        if (post != null) {
            edtTitle.setText(post.getTitle());
            edtDescription.setText(post.getDescription());
            spnRegion.setSelection(adapter.getPosition(post.getRegion()));
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

            boolean success = db.updatePost(postId, newTitle, newDesc, newRegion, post.getImagePath());

            if (success) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
