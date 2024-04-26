package com.steiner.app.Models.Items;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.R;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.databinding.ItemLayoutBinding;
import com.steiner.app.utils.Config;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private ItemLayoutBinding layoutBinding;

    private List<ItemsModel> itemsModelList;
    private Context context;
    private AppCompatActivity activity;
    private OnClickListeners clickListeners;
    private int Like = 0;

    public ItemsAdapter(List<ItemsModel> itemsModelList, Context context, AppCompatActivity activity, OnClickListeners clickListeners) {
        this.itemsModelList = itemsModelList;
        this.context = context;
        this.activity = activity;
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutBinding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(layoutBinding.getRoot());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        String id = itemsModelList.get(position).getId();
        String images = itemsModelList.get(position).getImages();
        String title = itemsModelList.get(position).getTitle();
        String details = itemsModelList.get(position).getDetails();
        String like = itemsModelList.get(position).getLike();
        String shear = itemsModelList.get(position).getShare();


        layoutBinding.likeImg.setOnClickListener(v -> {
            clickListeners.OnClick("ITEM", "LIKE", itemsModelList.get(position).id);
            notifyItemChanged(position);
        });
        layoutBinding.shareImg.setOnClickListener(v -> {
            clickListeners.OnClick("ITEM", "SHARE", itemsModelList.get(position).id);
            notifyItemChanged(position);

        });
        layoutBinding.inquiry.setOnClickListener(v -> {
            clickListeners.OnClick("ITEM", "INQUIRY", itemsModelList.get(position).id);
        });
        holder.itemView.setOnClickListener(v -> {
            clickListeners.OnClick("ADMIN_CLICK", "FULL", itemsModelList.get(position).id);
        });
        holder.setItems(id, images, title, details, like, shear);
    }

    @Override
    public int getItemCount() {
        return Math.min(itemsModelList.size(), 5);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void setItems(String id, String images, String title, String details, String like, String shear) {
            List<SlideModel> slideModelList = new ArrayList<>();

            String link = Config.ImageUrl;
            try {
                JSONArray jsonArray = new JSONArray(images);
                for (int i = 0; i < jsonArray.length(); i++) {
                    slideModelList.add(new SlideModel(link + jsonArray.getString(i), "", ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ItemsSliderAdapter adapter = new ItemsSliderAdapter(itemView.getContext(), slideModelList);
            layoutBinding.imageSlider.setSliderAdapter(adapter);

            layoutBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
            layoutBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            layoutBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_LEFT);
            layoutBinding.imageSlider.setIndicatorSelectedColor(android.graphics.Color.WHITE);
            layoutBinding.imageSlider.setIndicatorUnselectedColor(android.graphics.Color.GRAY);
            layoutBinding.imageSlider.setScrollTimeInSec(4);
            layoutBinding.imageSlider.startAutoCycle();

            boolean d = new DBHandler().chalkFavItemDataWithId(id);
            if (d) {
                layoutBinding.likeImg.setImageResource(R.drawable.ic_baseline_favorite_24);
                layoutBinding.likeImg.setColorFilter(ContextCompat.getColor(context, R.color.ColorRed));
            } else {
                layoutBinding.likeImg.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                layoutBinding.likeImg.setColorFilter(ContextCompat.getColor(context, R.color.ColorGray));
            }


            layoutBinding.title.setText(title);
            layoutBinding.details.setText(details);
            layoutBinding.likeCount.setText(like);
            layoutBinding.shareCount.setText(shear);


        }
    }
}
