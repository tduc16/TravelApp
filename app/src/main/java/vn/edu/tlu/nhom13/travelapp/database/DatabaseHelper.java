package vn.edu.tlu.nhom13.travelapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.nhom13.travelapp.models.Post;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng người dùng
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')");

        // Tạo bảng bài viết
        db.execSQL("CREATE TABLE Posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "region TEXT," +
                "imagePath TEXT," +
                "status TEXT," +
                "userId INTEGER)");

        // Dữ liệu mẫu
        db.execSQL("INSERT INTO Users (username, password, role) VALUES " +
                "('admin', '123456', 'admin')," +
                "('user', '123456', 'user')");

        db.execSQL("INSERT INTO Posts (title, description, region, imagePath, status, userId) VALUES " +
                "('Phố cổ Hà Nội', 'Khám phá ẩm thực và lịch sử phố cổ Hà Nội', 'Bắc', '', 'approved', 2)," +
                "('Vịnh Hạ Long', 'Kỳ quan thiên nhiên thế giới', 'Bắc', '', 'approved', 2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Posts");
        onCreate(db);
    }

    // 🔹 Thêm bài viết mới
    public boolean addPost(String title, String description, String region, String imagePath, String status, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("region", region);
        values.put("imagePath", imagePath);
        values.put("status", status);
        values.put("userId", userId);
        long result = db.insert("Posts", null, values);
        return result != -1;
    }

    // 🔹 Sửa bài viết
    public boolean updatePost(int postId, String title, String description, String region, String imagePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("region", region);
        values.put("imagePath", imagePath);
        int rows = db.update("Posts", values, "id = ?", new String[]{String.valueOf(postId)});
        return rows > 0;
    }

    // 🔹 Xoá bài viết
    public void deletePost(int postId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Posts", "id = ?", new String[]{String.valueOf(postId)});
    }

    // 🔹 Duyệt bài viết (admin)
    public void approvePost(int postId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "approved");
        db.update("Posts", values, "id = ?", new String[]{String.valueOf(postId)});
    }

    // 🔹 Lấy bài viết đã duyệt
    public List<Post> getApprovedPosts() {
        return getPostsByStatus("approved");
    }

    // 🔹 Lấy bài viết chờ duyệt
    public List<Post> getPendingPosts() {
        return getPostsByStatus("pending");
    }

    // 🔹 Lấy bài viết theo userId
    public List<Post> getPostsByUserId(int userId) {
        List<Post> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Posts WHERE userId = ?", new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            list.add(cursorToPost(cursor));
        }
        cursor.close();
        return list;
    }

    // 🔹 Lấy bài viết theo status
    private List<Post> getPostsByStatus(String status) {
        List<Post> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Posts WHERE status = ?", new String[]{status});
        while (cursor.moveToNext()) {
            list.add(cursorToPost(cursor));
        }
        cursor.close();
        return list;
    }

    // 🔹 Đăng ký người dùng
    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("role", role);
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    // 🔹 Đăng nhập trả về vai trò (admin/user)
    public String loginUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM Users WHERE username = ? AND password = ?",
                new String[]{username, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    // 🔹 Lấy userId từ username
    public int getUserId(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    // 🔹 Convert Cursor → Post
    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();
        post.setId(cursor.getInt(0));
        post.setTitle(cursor.getString(1));
        post.setDescription(cursor.getString(2));
        post.setRegion(cursor.getString(3));
        post.setImagePath(cursor.getString(4));
        post.setStatus(cursor.getString(5));
        post.setUserId(cursor.getInt(6));
        return post;
    }

    public Post getPostById(int postId) {
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Posts WHERE id = ?", new String[]{String.valueOf(postId)})) {
            if (cursor.moveToFirst()) {
                return cursorToPost(cursor);
            }
            return null;
        }
    }
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE username = ?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}


