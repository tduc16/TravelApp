package vn.edu.tlu.nhom13.travelapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import vn.edu.tlu.nhom13.travelapp.models.Comment;
import android.util.Log;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;


import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.nhom13.travelapp.models.Post;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelApp.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')");

        db.execSQL("CREATE TABLE Posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "region TEXT," +
                "imagePath TEXT," +
                "status TEXT," +
                "userId INTEGER)");

        db.execSQL("CREATE TABLE FavoritePosts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "postId INTEGER)");

        db.execSQL("INSERT INTO Users (username, password, role) VALUES " +
                "('admin', '123456', 'admin')," +
                "('user', '123456', 'user')");
        db.execSQL("CREATE TABLE Comments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "postId INTEGER," +
                "userId INTEGER," +
                "username TEXT," +
                "content TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");


        db.execSQL("INSERT INTO Posts (title, description, region, imagePath, status, userId) VALUES " +
                "('Phố cổ Hà Nội', 'Khám phá ẩm thực và lịch sử phố cổ Hà Nội', 'Bắc', '', 'approved', 2)," +
                "('Vịnh Hạ Long', 'Kỳ quan thiên nhiên thế giới', 'Bắc', '', 'approved', 2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Posts");
        db.execSQL("DROP TABLE IF EXISTS FavoritePosts");
        db.execSQL("DROP TABLE IF EXISTS Comments");
        onCreate(db);
    }

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

    public void deletePost(int postId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Posts", "id = ?", new String[]{String.valueOf(postId)});
    }

    public void approvePost(int postId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "approved");
        db.update("Posts", values, "id = ?", new String[]{String.valueOf(postId)});
    }

    public List<Post> getApprovedPosts() {
        return getPostsByStatus("approved");
    }

    public List<Post> getPendingPosts() {
        return getPostsByStatus("pending");
    }

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

    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("role", role);
        long result = db.insert("Users", null, values);
        return result != -1;
    }

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

    // 🔹 Yêu thích - kiểm tra bài viết có được user yêu thích không
    public boolean isFavoritePost(int userId, int postId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM FavoritePosts WHERE userId = ? AND postId = ?",
                new String[]{String.valueOf(userId), String.valueOf(postId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // 🔹 Yêu thích - thêm bài viết vào danh sách yêu thích
    public void addFavoritePost(int userId, int postId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("postId", postId);
        db.insert("FavoritePosts", null, values);
    }

    // 🔹 Yêu thích - xoá bài viết khỏi danh sách yêu thích
    public void removeFavoritePost(int userId, int postId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("FavoritePosts", "userId = ? AND postId = ?",
                new String[]{String.valueOf(userId), String.valueOf(postId)});
    }

    // 🔹 Lấy danh sách bài viết yêu thích
    public List<Post> getFavoritePosts(int userId) {
        List<Post> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT P.* FROM Posts P " +
                "JOIN FavoritePosts F ON P.id = F.postId " +
                "WHERE F.userId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            list.add(cursorToPost(cursor));
        }
        cursor.close();
        return list;
    }

    // 🔹 Thêm bình luận
    public boolean addComment(int postId, int userId, String username, String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("postId", postId);
        values.put("userId", userId);
        values.put("username", username);  // thêm username
        values.put("content", content);
        values.put("timestamp", System.currentTimeMillis());

        Log.d("COMMENT_ADD", "postId: " + postId + ", userId: " + userId + ", username: " + username + ", content: " + content);

        long result = db.insert("Comments", null, values);
        db.close();
        return result != -1;
    }


    // 🔹 Lấy danh sách bình luận theo postId (đã sửa để lấy đầy đủ id, userId)
    public List<Comment> getCommentsForPost(int postId) {
        List<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, postId, userId, username, content, timestamp FROM Comments WHERE postId = ? ORDER BY timestamp DESC",
                new String[]{String.valueOf(postId)}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int fetchedPostId = cursor.getInt(cursor.getColumnIndexOrThrow("postId"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                Comment comment = new Comment(id, fetchedPostId, userId, username, content, timestamp);
                comments.add(comment);

                Log.d("COMMENT_FETCH", "id: " + id + ", userId: " + userId + ", username: " + username + ", content: " + content + ", timestamp: " + timestamp);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return comments;
    }

    public boolean deleteComment(int postId, String username, String content, String timestamp) {
        return false;
    }

    // 🔹 Cập nhật bình luận (nội dung mới)
    public boolean updateComment(int postId, String username, String oldContent, String timestamp, String newContent) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", newContent);
        int rowsAffected = db.update(
                "Comments",
                values,
                "postId = ? AND username = ? AND content = ? AND timestamp = ?",
                new String[]{String.valueOf(postId), username, oldContent, timestamp}
        );
        db.close();
        return rowsAffected > 0;
    }
}
