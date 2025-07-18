package vn.edu.tlu.nhom13.travelapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class ApprovePostAdapter extends RecyclerView.Adapter<ApprovePostAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private DatabaseHelper db;
    private Runnable refreshCallback;

    public ApprovePostAdapter(Context context, List<Post> postList, DatabaseHelper db, Runnable refreshCallback) {
        this.context = context;
        this.postList = postList;
        this.db = db;
        this.refreshCallback = refreshCallback;
    }

    public void setData(List<Post> newPostList) {
        this.postList = newPostList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.txtTitle.setText(post.getTitle());
        holder.txtRegion.setText(post.getRegion());
        holder.txtDescription.setText(post.getDescription());

        // ✅ Load ảnh
        String imagePath = post.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            if (imagePath.startsWith("android.resource://")) {
                Glide.with(context).load(Uri.parse(imagePath)).into(holder.imgPost);
            } else {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Glide.with(context).load(imgFile).into(holder.imgPost);
                } else {
                    holder.imgPost.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        } else {
            holder.imgPost.setImageResource(R.drawable.ic_launcher_background);
        }

        // ✅ Nút duyệt
        holder.btnApprove.setVisibility(View.VISIBLE);
        holder.btnApprove.setOnClickListener(v -> {
            db.approvePost(post.getId());
            Toast.makeText(context, "Đã duyệt bài viết", Toast.LENGTH_SHORT).show();
            if (refreshCallback != null) {
                refreshCallback.run(); // Gọi Activity load lại danh sách
            }
        });

        // ✅ Nút xóa
        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.btnDelete.setOnClickListener(v -> {
            db.deletePost(post.getId());
            Toast.makeText(context, "Đã xóa bài viết", Toast.LENGTH_SHORT).show();
            if (refreshCallback != null) {
                refreshCallback.run(); // Gọi Activity load lại danh sách
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtRegion, txtDescription;
        ImageView imgPost;
        Button btnApprove, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRegion = itemView.findViewById(R.id.txtRegion);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgPost = itemView.findViewById(R.id.imgPost);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
