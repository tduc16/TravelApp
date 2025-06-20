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

        // Lấy dữ liệu từ intent
        Intent intent = getIntent();
        postId = intent.getIntExtra("postId", -1);
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("description");
        String region = intent.getStringExtra("region");
        imagePath = intent.getStringExtra("imagePath");

        edtTitle.setText(title);
        edtDescription.setText(desc);
        spnRegion.setSelection(adapter.getPosition(region));
        if (imagePath != null) {
            imgPreview.setImageURI(Uri.parse(imagePath));
        }

        btnUpdate.setOnClickListener(v -> {
            String newTitle = edtTitle.getText().toString().trim();
            String newDesc = edtDescription.getText().toString().trim();
            String newRegion = spnRegion.getSelectedItem().toString();

            if (newTitle.isEmpty() || newDesc.isEmpty()) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper db = new DatabaseHelper(this);
            db.updatePost(postId, newTitle, newDesc, newRegion, imagePath); // sửa trong CSDL

            Toast.makeText(this, "Đã cập nhật bài viết", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

