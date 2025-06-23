package vn.edu.tlu.nhom13.travelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.models.Comment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> comments;
    private final String currentUsername;
    private final Context context;

    public interface OnCommentDeleteListener {
        void onCommentDelete(int position);
    }

    public interface OnCommentEditListener {
        void onCommentEdit(int position);
    }

    private OnCommentDeleteListener deleteListener;
    private OnCommentEditListener editListener;

    public void setOnCommentDeleteListener(OnCommentDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnCommentEditListener(OnCommentEditListener listener) {
        this.editListener = listener;
    }

    public CommentAdapter(Context context, List<Comment> comments, int postId) {
        this.context = context;
        this.comments = comments;
        this.currentUsername = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                .getString("username", "");
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.txtUsername.setText(comment.getUsername());
        holder.txtComment.setText(comment.getContent());

        // Chỉ hiện nút Sửa/Xoá nếu là bình luận của chính user
        if (comment.getUsername().equals(currentUsername)) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
        }

        // Sự kiện xoá
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onCommentDelete(position);
            }
        });

        // Sự kiện sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onCommentEdit(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        TextView txtComment;
        Button btnDelete, btnEdit;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtComment = itemView.findViewById(R.id.txtComment);
            btnDelete = itemView.findViewById(R.id.btnDeleteComment);
            btnEdit = itemView.findViewById(R.id.btnEditComment);
        }
    }
}
