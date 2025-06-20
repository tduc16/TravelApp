package vn.edu.tlu.nhom13.travelapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;
    private List<Post> postListFull;
    private int currentUserId = -1;
    private OnEditClickListener editClickListener;

    public interface OnEditClickListener {
        void onEditClick(Post post);
    }

    public PostAdapter(Context context, List<Post> postList, int currentUserId, OnEditClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.postListFull = new ArrayList<>(postList); // sao lưu để lọc
        this.currentUserId = currentUserId;
        this.editClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.txtTitle.setText(post.getTitle());
        holder.txtRegion.setText(post.getRegion());
        holder.txtDescription.setText(post.getDescription());

        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            Uri imageUri = Uri.parse(post.getImagePath());
            Glide.with(context).load(imageUri).into(holder.imgPost);
        } else {
            holder.imgPost.setImageResource(R.drawable.ic_launcher_background);
        }

        // Hiện nút sửa/xóa nếu đúng userId
        if (post.getUserId() == currentUserId) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> {
                if (editClickListener != null) {
                    editClickListener.onEditClick(post);
                }
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa bài viết này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            DatabaseHelper db = new DatabaseHelper(context);
                            db.deletePost(post.getId());

                            postList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, postList.size());

                            Toast.makeText(context, "Đã xóa bài viết", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });

        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void filter(String keyword) {
        postList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            postList.addAll(postListFull);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            for (Post post : postListFull) {
                if (post.getTitle().toLowerCase().contains(lowerKeyword)) {
                    postList.add(post);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtRegion, txtDescription;
        ImageView imgPost;
        ImageButton btnEdit, btnDelete;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRegion = itemView.findViewById(R.id.txtRegion);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgPost = itemView.findViewById(R.id.imgPost);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}






