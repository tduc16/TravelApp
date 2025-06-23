package vn.edu.tlu.nhom13.travelapp.models;

public class Comment {
    private int id;          // ID comment
    private int postId;      // ID bài viết
    private int userId;      // ID người bình luận
    private String username; // tên người dùng
    private String content;  // nội dung
    private String timestamp; // thời gian

    public Comment(int id, int postId, int userId, String username, String content, String timestamp) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getter đầy đủ
    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
