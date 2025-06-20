package vn.edu.tlu.nhom13.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;

public class AddPostActivity extends AppCompatActivity {

    EditText edtTitle, edtDescription;
    Spinner spnRegion;
    ImageView imgPreview;
    Button btnChooseImage, btnSubmit;
    Uri imageUri = null;

    static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        spnRegion = findViewById(R.id.spnRegion);
        imgPreview = findViewById(R.id.imgPreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set vùng miền
        String[] regions = {"Bắc", "Trung", "Nam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRegion.setAdapter(adapter);

        // Chọn ảnh
        btnChooseImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Thêm bài viết
        btnSubmit.setOnClickListener(view -> {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String region = spnRegion.getSelectedItem().toString();

            if (title.isEmpty() || description.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ và chọn ảnh", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy username từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);

            if (username == null) {
                Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper db = new DatabaseHelper(this);
            int userId = db.getUserId(username); // Lấy userId từ username

            String imagePath = imageUri.toString();

            boolean result = db.addPost(title, description, region, imagePath, "pending", userId);
            if (result) {
                Toast.makeText(this, "Đã thêm bài viết chờ duyệt", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri);
        }
    }
}

