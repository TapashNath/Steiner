package com.steiner.app.Models.Image;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.R;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class ImageListGridViewAdapter extends RecyclerView.Adapter<ImageListGridViewAdapter.ViewerHolder> implements Filterable {
    List<GalleryModel> productModels;
    List<GalleryModel> productModelsFilter;
    OnClickListeners listener;

    public ImageListGridViewAdapter(List<GalleryModel> productModels, OnClickListeners listener) {
        this.productModels = productModels;
        this.productModelsFilter = productModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view_layout, parent, false);
        return new ViewerHolder(view);

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String cementer = constraint.toString();
                if (cementer.isEmpty()) {
                    productModelsFilter = productModels;
                } else {
                    List<GalleryModel> filterList = new ArrayList<>();
                    for (GalleryModel row : productModels) {
                        if (row.getId().toLowerCase().contains(cementer.toLowerCase()) ) {
                            filterList.add(row);
                        }
                    }
                    productModelsFilter = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productModelsFilter;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productModelsFilter = (ArrayList<GalleryModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewerHolder holder, int position) {

        String image = productModelsFilter.get(position).getImage();


        holder.setProduct(image);


    }


    @Override
    public int getItemCount() {
        return productModelsFilter.size();
    }

    public class ViewerHolder extends RecyclerView.ViewHolder {
        private ImageView imageView ,share;
        public ViewerHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            share = itemView.findViewById(R.id.imageShear);

            share.setOnClickListener(v -> {
                listener.OnClick("IMAGES", "SHARE", productModelsFilter.get(getAdapterPosition()).getId());
            });
            itemView.setOnClickListener(v -> {
                listener.OnClick("IMAGES", "CLICK", productModelsFilter.get(getAdapterPosition()).getId());
            });

        }

        private void setProduct(String image) {
            String link = Config.ImageUrl;
            Glide.with(MyApplication.context)
                    .load(link + image)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }
    }


}
