package com.example.touristphotoassistant.ui.photocard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristphotoassistant.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<PhotoX> photoList;
    public RecyclerviewAdapter(Context context){
        mContext = context;
        photoList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.task_item,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PhotoX task = photoList.get(position);
        holder.tvPhoto.setImageBitmap(task.getPhoto());
        holder.tvPhotoDesc.setText(task.getPhotoDesc());
    }
    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void setPhotoList(List<PhotoX> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPhotoDesc;
        private ImageView tvPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPhotoDesc = itemView.findViewById(R.id.photo_desc);
            tvPhoto = itemView.findViewById(R.id.photo);
        }
    }
}
