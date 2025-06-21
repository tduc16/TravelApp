package vn.edu.tlu.nhom13.travelapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final Context context;
    private final List<Post> postList;
    private final List<Post> postListFull;
    private final OnEditClickListener editClickListener;
    private final OnAddClickListener addClickListener;
    private int currentUserId;

    public interface OnEditClickListener {
        void onEditClick(Post post);
    }

    public interface OnAddClickListener {
        void onAddClick(Post post);
    }

    public PostAdapter(Context context, List<Post> postList, int currentUserId,
                       OnEditClickListener editClickListener, OnAddClickListener addClickListener) {
        this.context = context;
        this.postList = postList;
        this.postListFull = new ArrayList<>(postList);
        this.currentUserId = currentUserId;
        this.editClickListener = editClickListener;
        this.addClickListener = addClickListener;
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

        // Hiển thị nút chỉnh sửa nếu là bài viết của chính user
        if (post.getUserId() == currentUserId) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(v -> {
                if (editClickListener != null) {
                    editClickListener.onEditClick(post);
                }
            });

            // Hiển thị nút thêm
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnAdd.setOnClickListener(v -> {
                if (addClickListener != null) {
                    addClickListener.onAddClick(post);
                }
            });
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    public void setData(List<Post> newPostList) {
        postList.clear();
        postList.addAll(newPostList);

        postListFull.clear();
        postListFull.addAll(newPostList);

        notifyDataSetChanged();
    }


    public void filter(String keyword) {
        postList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            postList.addAll(postListFull);
        } else {
            String lowerKeyword = keyword.toLowerCase().trim();
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
        ImageButton btnEdit, btnAdd;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRegion = itemView.findViewById(R.id.txtRegion);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgPost = itemView.findViewById(R.id.imgPost);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnAdd = itemView.findViewById(R.id.btnAdd); // ImageButton phải tồn tại trong item_post.xml
        }
    }
}