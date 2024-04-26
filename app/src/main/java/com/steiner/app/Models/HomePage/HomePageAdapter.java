package com.steiner.app.Models.HomePage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.steiner.app.Models.Category.CategoryGridViewAdapter;
import com.steiner.app.Models.Category.CategoryGridViewModel;
import com.steiner.app.Models.Items.ItemsAdapter;
import com.steiner.app.Models.Items.ItemsModel;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.R;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.Slider.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private AppCompatActivity activity;
    private OnClickListeners listeners;

    public HomePageAdapter(List<HomePageModel> homePageModelList, AppCompatActivity activity, OnClickListeners listeners) {
        this.homePageModelList = homePageModelList;
        this.activity = activity;
        this.listeners = listeners;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getTYPE()) {
            case 0:
                return HomePageModel.BANNER_SLIDER_VIEW;
            case 1:
                return HomePageModel.CATEGORY_LIST_VIEW;
            case 2:
                return HomePageModel.FINISH_WORK_LIST_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER_VIEW:
                View BANNER_SLIDER_VIEW = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_add_layout, parent, false);
                return new BannerSliderViewHolder(BANNER_SLIDER_VIEW);
            case HomePageModel.CATEGORY_LIST_VIEW:
                View CATEGORY_LIST_VIEW = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_category_layout, parent, false);
                return new TopCategoryListView(CATEGORY_LIST_VIEW);
            case HomePageModel.FINISH_WORK_LIST_VIEW:
                View FINISH_WORK_LIST_VIEW = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_layout, parent, false);
                return new ItemsListView(FINISH_WORK_LIST_VIEW);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getTYPE()) {
            case HomePageModel.BANNER_SLIDER_VIEW:
                List<SlideModel> slide_home_models = homePageModelList.get(position).getSlideModels();
                ((BannerSliderViewHolder) holder).setSliderView(slide_home_models, listeners);
                break;
            case HomePageModel.CATEGORY_LIST_VIEW:
                List<CategoryGridViewModel> categoryGridViewModels = homePageModelList.get(position).getCategoryGridViewModels();
                String Top_Category_name = homePageModelList.get(position).getCategoryName();
                ((TopCategoryListView) holder).setCategoryListView(Top_Category_name,  categoryGridViewModels, activity, listeners);
                break;
            case HomePageModel.FINISH_WORK_LIST_VIEW:
                List<ItemsModel> itemsModelList = homePageModelList.get(position).getItemsModelList();
                String name = homePageModelList.get(position).getListName();
                ((ItemsListView)holder).setItems(name,itemsModelList,activity, listeners);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    private static class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private SliderView sliderView;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderView = itemView.findViewById(R.id.imageSlider);
        }

        private void setSliderView(List<SlideModel> slide_home_modelArrayList, OnClickListeners listeners) {
            SliderAdapter adapter = new SliderAdapter(itemView.getContext(), slide_home_modelArrayList);
            sliderView.setSliderAdapter(adapter);

            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_LEFT);
            sliderView.setIndicatorSelectedColor(android.graphics.Color.WHITE);
            sliderView.setIndicatorUnselectedColor(android.graphics.Color.GRAY);
            sliderView.setScrollTimeInSec(4);
            sliderView.startAutoCycle();
        }


    }

    private static class TopCategoryListView extends RecyclerView.ViewHolder {
        private TextView GirdView_Category_Title, CategoryViewAllButton;
        private RecyclerView Grid_viewProductView;

        public TopCategoryListView(@NonNull View itemView) {
            super(itemView);
            GirdView_Category_Title = itemView.findViewById(R.id.GirdView_Category_Title);
            CategoryViewAllButton = itemView.findViewById(R.id.CategoryViewAllButton);
            Grid_viewProductView = itemView.findViewById(R.id.Category_GrideView);
        }

        private void setCategoryListView(String name, List<CategoryGridViewModel> categoryGridViewModels, AppCompatActivity activity, OnClickListeners listeners) {
            GirdView_Category_Title.setText(name);

            if (categoryGridViewModels.size()>0){
                Grid_viewProductView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
                CategoryGridViewAdapter adapter = new CategoryGridViewAdapter(categoryGridViewModels, itemView.getContext(), activity, listeners);
                Grid_viewProductView.setAdapter(adapter);
            }


            if (categoryGridViewModels.size()> 3){
                CategoryViewAllButton.setVisibility(View.VISIBLE);
            }else {
                CategoryViewAllButton.setVisibility(View.GONE);
            }
            CategoryViewAllButton.setOnClickListener(view -> listeners.OnClick("CATE","CLICK",categoryGridViewModels.get(getAdapterPosition()).getId()));


        }
    }

    private static class ItemsListView extends RecyclerView.ViewHolder {
        private TextView title, seeMore;
        private RecyclerView list;

        public ItemsListView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemName);
            seeMore = itemView.findViewById(R.id.itemButton);
            list = itemView.findViewById(R.id.itemView);
        }


        public void setItems(String name, List<ItemsModel> itemsModelList, AppCompatActivity activity, OnClickListeners listeners) {
            title.setText(name);
            LinearLayoutManager AllViewViewLayout = new LinearLayoutManager(itemView.getContext());
            AllViewViewLayout.setOrientation(RecyclerView.VERTICAL);
            list.setLayoutManager(AllViewViewLayout);

            ItemsAdapter itemsAdapter = new ItemsAdapter(itemsModelList, itemView.getContext(),activity, listeners);
            list.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();


            seeMore.setVisibility(View.GONE);

        }
    }
}
