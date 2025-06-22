package vn.edu.tlu.nhom13.travelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class ManagePostAdapter extends RecyclerView.Adapter<ManagePostAdapter.ViewHolder> {

    private final Context context;
    private final List<Post> postList;
    private final DatabaseHelper dbHelper;

    public ManagePostAdapter(Context context, List<Post> postList, DatabaseHelper dbHelper) {
        this.context = context;
        this.postList = postList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ManagePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePostAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.txtTitle.setText(post.getTitle());
        holder.txtRegion.setText(post.getRegion());
        holder.txtDescription.setText(post.getDescription());
        holder.imgPost.setImageResource(R.drawable.ic_launcher_background); // hoặc load ảnh nếu có

        // Hiện nút XÓA
        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deletePost(post.getId());          // Xóa khỏi DB
            postList.remove(position);                  // Xóa khỏi danh sách
            notifyItemRemoved(position);                // Cập nhật UI
            Toast.makeText(context, "Đã xóa bài viết", Toast.LENGTH_SHORT).show();
        });

        // Ẩn các nút khác nếu cần
        holder.btnEdit.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtRegion, txtDescription;
        ImageView imgPost;
        Button btnDelete;
        View btnEdit; // chỉ để ẩn nếu layout có

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRegion = itemView.findViewById(R.id.txtRegion);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imgPost = itemView.findViewById(R.id.imgPost);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);   // để ẩn nếu có trong layout
        }
    }
}
