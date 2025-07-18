package vn.edu.tlu.nhom13.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.adapter.PostAdapter;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;

    private ImageView imgAvatar;
    private TextView txtUsername;

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

        txtUsername.setText(username);

        if ("user".equals(role)) {
            imgAvatar.setOnClickListener(v -> showUserPopupMenu());
        } else {
            imgAvatar.setOnClickListener(v -> showAdminPopupMenu());
        }

        setupRecyclerView();
        setupSearchFilter();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        edtSearch = findViewById(R.id.edtSearch);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtUsername = findViewById(R.id.txtUsername);

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
            Intent intent = new Intent(MainActivity.this, EditPostActivity.class);
            intent.putExtra("postId", post.getId());
            intent.putExtra("userId", userId);
            startActivity(intent);
        }, post -> {
            Intent intent = new Intent(MainActivity.this, AddPostActivity.class);intent.putExtra("userId", userId);
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

    private void showUserPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, imgAvatar);
        popupMenu.getMenuInflater().inflate(R.menu.user_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_add) {
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;

            } else if (id == R.id.menu_favorite) {
                Intent intent = new Intent(MainActivity.this, FavoritePostsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;

            } else if (id == R.id.menu_logout) {
                Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }


    private void showAdminPopupMenu() {
        PopupMenu popup = new PopupMenu(this, imgAvatar);
        popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_add) {
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            } else if (id == R.id.menu_approve) {
                startActivity(new Intent(this, ApprovePostsActivity.class));
            } else if (id == R.id.menu_manage) {
                startActivity(new Intent(this, ManagePostsActivity.class));
            }
            return true;
        });

        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadPostList();
    }

    private void reloadPostList() {
        postList = dbHelper.getApprovedPosts();
        adapter.setData(postList);
        adapter.notifyDataSetChanged();
    }
}