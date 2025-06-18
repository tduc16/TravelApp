package vn.edu.tlu.nhom13.travelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.nhom13.travelapp.R;
import vn.edu.tlu.nhom13.travelapp.database.DatabaseHelper;
import vn.edu.tlu.nhom13.travelapp.models.Post;

public class ApprovePostAdapter extends RecyclerView.Adapter<ApprovePostAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private DatabaseHelper db;

    public ApprovePostAdapter(Context context, List<Post> postList, DatabaseHelper db) {
        this.context = context;
        this.postList = postList;
        this.db = db;
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

        holder.btnApprove.setVisibility(View.VISIBLE);
        holder.btnApprove.setOnClickListener(v -> {
            db.approvePost(post.getId());
            postList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Đã duyệt bài viết", Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.btnDelete.setOnClickListener(v -> {
            db.deletePost(post.getId());
            postList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Đã xóa bài viết", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtRegion, txtDescription;
        Button btnApprove, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRegion = itemView.findViewById(R.id.txtRegion);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
