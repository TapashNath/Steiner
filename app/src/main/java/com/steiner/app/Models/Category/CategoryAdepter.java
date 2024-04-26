package com.steiner.app.Models.Category;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.List;

public class CategoryAdepter extends RecyclerView.Adapter<CategoryAdepter.ViewHolder> implements Filterable {
    private Context context;
    private List<CategoryModel> categoryModelList;
    private List<CategoryModel> categoryModelListFilter;
    private OnClickListeners clickListener;
    private AppCompatActivity activity;

    public CategoryAdepter(Context context, List<CategoryModel> categoryModelList, OnClickListeners clickListener, AppCompatActivity activity) {
        this.context = context;
        this.categoryModelList = categoryModelList;
        this.categoryModelListFilter = categoryModelList;
        this.clickListener = clickListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String cementer = constraint.toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (cementer.isEmpty()) {
                        categoryModelListFilter = categoryModelList;
                    } else {
                        List<CategoryModel> filterList = new ArrayList<>();
                        for (CategoryModel row : categoryModelList) {
                            if (row.getCategoryTitle().toLowerCase().contains(cementer.toLowerCase())) {
                                filterList.add(row);
                            }
                        }
                        categoryModelListFilter = filterList;
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = categoryModelListFilter;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                categoryModelListFilter = (ArrayList<CategoryModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String CategoryId = categoryModelListFilter.get(position).getCategoryId();
        String Image = categoryModelListFilter.get(position).getCategoryImage();
        String Title = categoryModelListFilter.get(position).getCategoryTitle();

        holder.setCategoryItems(CategoryId, Image, Title);
        holder.itemView.setOnClickListener(v -> clickListener.OnClick("CATEGORY", "CLICK", CategoryId));

    }

    @Override
    public int getItemCount() {
        return categoryModelListFilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView CategoryImage;
        private TextView CategoryName;
        private TextView CategorySebText;
        private ConstraintLayout CategoryItems;
        private SPDataSave spDataSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CategoryImage = itemView.findViewById(R.id.CategoryImage);
            CategoryName = itemView.findViewById(R.id.CategoryTitle);
            CategorySebText = itemView.findViewById(R.id.CategorySubTitle);
            CategoryItems = itemView.findViewById(R.id.CategoryItem);
            spDataSave = new SPDataSave();

            CategoryItems.setOnClickListener(view -> {

            });
        }

        private void setCategoryItems(String id, String image, String name) {
            String link = Config.ImageUrl;
            Glide.with(MyApplication.context)
                    .load(link + image)
                    .placeholder(R.drawable.placeholder)
                    .into(CategoryImage);
            CategoryName.setText(name);
        }
    }
}
