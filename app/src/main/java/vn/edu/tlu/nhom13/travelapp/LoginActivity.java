package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String user = edtUsername.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = dbHelper.loginUser(user, pass);
            if (role != null) {
                Toast.makeText(this, "Đăng nhập thành công: " + role, Toast.LENGTH_SHORT).show();

                int userId = dbHelper.getUserId(user);

                // ✅ Lưu username vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", user);
                editor.putInt("userId", userId);
                editor.putString("role", role);
                editor.apply();

                // Chuyển sang MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}

