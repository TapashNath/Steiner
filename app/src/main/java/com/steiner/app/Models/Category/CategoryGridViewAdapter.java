package com.steiner.app.Models.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.R;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;
import com.steiner.app.utils.SPDataSave;

import java.util.List;

public class CategoryGridViewAdapter extends RecyclerView.Adapter<CategoryGridViewAdapter.ViewHolder> {

    private List<CategoryGridViewModel> categoryGridViewModelList;
    private Context context;
    private AppCompatActivity activity;
    private OnClickListeners listeners;

    public CategoryGridViewAdapter(List<CategoryGridViewModel> categoryGridViewModelList, Context context, AppCompatActivity activity, OnClickListeners listeners) {
        this.categoryGridViewModelList = categoryGridViewModelList;
        this.context = context;
        this.activity = activity;
        this.listeners = listeners;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridViewAdapter.ViewHolder holder, int position) {

        String id = categoryGridViewModelList.get(position).getId();
        String image = categoryGridViewModelList.get(position).getImage();
        String name = categoryGridViewModelList.get(position).getName();

        holder.setProduct(id,image,name);
        holder.itemView.setOnClickListener(v -> listeners.OnClick("CATEGORY","CLICK",categoryGridViewModelList.get(position).getId()));


    }


    @Override
    public int getItemCount() {

        if (categoryGridViewModelList.size() > 9){
            return 9;
        }else if (categoryGridViewModelList.size() > 6) {
            return 6;
        }else if (categoryGridViewModelList.size() > 3) {
            return 3;
        }else {
           return categoryGridViewModelList.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ProductImage;
        private TextView ProductTitle, ProductSubTitle;
        private ConstraintLayout gardLayout;
        private SPDataSave spDataSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductImage = itemView.findViewById(R.id.GRD_Product_Image);
            ProductTitle = itemView.findViewById(R.id.GRD_Product_title);
            ProductSubTitle = itemView.findViewById(R.id.GRD_Product_subtitle);
            gardLayout = itemView.findViewById(R.id.gardLayout);
            spDataSave = new SPDataSave();


        }


        private void setProduct(String id, String image, String name) {
            ProductTitle.setText(name);
            ProductSubTitle.setText(name);
            String link = Config.ImageUrl;
            Glide.with(MyApplication.context)
                    .load(link+image)
                    .placeholder(R.drawable.placeholder)
                    .into(ProductImage);
        }
    }
}
