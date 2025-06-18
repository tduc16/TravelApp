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

    // Tạo bảng Users
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')";
        db.execSQL(CREATE_USERS_TABLE);
        String CREATE_POSTS_TABLE = "CREATE TABLE Posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT, description TEXT, region TEXT," +
                "imagePath TEXT, status TEXT, userId INTEGER)";
        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL("INSERT INTO Posts (title, description, region, imagePath, status, userId) VALUES" +
                "('Phố cổ Hà Nội', 'Khám phá ẩm thực và lịch sử phố cổ Hà Nội', 'Bắc', '', 'approved', 2)," +
                "('Vịnh Hạ Long', 'Kỳ quan thiên nhiên thế giới với hàng nghìn đảo đá', 'Bắc', '', 'approved', 2)," +
                "('Sapa mùa lúa chín', 'Ruộng bậc thang tuyệt đẹp và văn hóa dân tộc thiểu số', 'Bắc', '', 'approved', 2)," +
                "('Đà Nẵng biển Mỹ Khê', 'Bãi biển đẹp nhất hành tinh, gần Bà Nà Hills', 'Trung', '', 'approved', 2)," +
                "('Hội An cổ kính', 'Phố cổ lung linh đèn lồng về đêm, di sản văn hóa', 'Trung', '', 'approved', 2)," +
                "('Huế - Kinh đô xưa', 'Đại Nội, lăng tẩm, chùa Thiên Mụ', 'Trung', '', 'approved', 2)," +
                "('TP.HCM sôi động', 'Cuộc sống năng động, chợ Bến Thành, Bitexco', 'Nam', '', 'approved', 2)," +
                "('Cần Thơ miền Tây', 'Chợ nổi Cái Răng, miệt vườn trái cây', 'Nam', '', 'approved', 2);");

        // Thêm tài khoản admin mặc định
        db.execSQL("INSERT INTO Users (username, password, role) VALUES ('admin', '123456', 'admin')");
        db.execSQL("INSERT INTO Users (username, password, role) VALUES ('user', '123456', 'user')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Posts");
        onCreate(db);
    }

    // Đăng ký người dùng
    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("role", role);
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    // Đăng nhập người dùng
    public String loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM Users WHERE username=? AND password=?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }
    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1; // Không tìm thấy
    }
    public List<Post> getApprovedPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Posts WHERE status = 'approved'", null);
        if (cursor.moveToFirst()) {
            do {
                Post post = new Post();
                post.setId(cursor.getInt(0));
                post.setTitle(cursor.getString(1));
                post.setDescription(cursor.getString(2));
                post.setRegion(cursor.getString(3));
                post.setImagePath(cursor.getString(4));
                post.setStatus(cursor.getString(5));
                post.setUserId(cursor.getInt(6));
                postList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return postList;
    }
}
