package vn.edu.tlu.nhom13.travelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
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
            Glide.with(context).load(post.getImagePath()).into(holder.imgPost);
        } else {
            holder.imgPost.setImageResource(R.drawable.ic_launcher_background); // mặc định
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtRegion, txtDescription;
        ImageView imgPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRegion = itemView.findViewById(R.id.txtRegion);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgPost = itemView.findViewById(R.id.imgPost);
        }
    }
}
