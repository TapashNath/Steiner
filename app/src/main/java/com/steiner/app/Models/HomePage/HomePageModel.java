package com.steiner.app.Models.HomePage;

import com.steiner.app.Models.Category.CategoryGridViewModel;
import com.steiner.app.Models.Items.ItemsModel;
import com.steiner.app.Slider.SlideModel;

import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER_VIEW = 0;
    public static final int CATEGORY_LIST_VIEW = 1;
    public static final int FINISH_WORK_LIST_VIEW = 2;
    public static final int OFFER_LIST_VIEW = 3;
    private int TYPE;

    //////////////////////////////////////////
    private List<SlideModel> slideModels;
    public HomePageModel(int TYPE, List<SlideModel> slideModels) {
        this.TYPE = TYPE;
        this.slideModels = slideModels;
    }
    public static int getBannerSliderView() {
        return BANNER_SLIDER_VIEW;
    }
    public int getTYPE() {
        return TYPE;
    }
    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }
    public List<SlideModel> getSlideModels() {
        return slideModels;
    }
    public void setSlideModels(List<SlideModel> slideModels) {
        this.slideModels = slideModels;
    }
    /////////////////////////////////////////

    //////////////////////Category List layouts
    private String CategoryName;
    private List<CategoryGridViewModel> categoryGridViewModels;

    public HomePageModel(int TYPE, String categoryName, List<CategoryGridViewModel> categoryGridViewModels) {
        this.TYPE = TYPE;
        CategoryName = categoryName;
        this.categoryGridViewModels = categoryGridViewModels;
    }
    public static int getCategoryListView() {
        return CATEGORY_LIST_VIEW;
    }
    public String getCategoryName() {
        return CategoryName;
    }
    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
    public List<CategoryGridViewModel> getCategoryGridViewModels() {
        return categoryGridViewModels;
    }
    public void setCategoryGridViewModels(List<CategoryGridViewModel> categoryGridViewModels) {
        this.categoryGridViewModels = categoryGridViewModels;
    }
    //////////////////////Category List layouts

    //////////////////////Items List layouts
    private List<ItemsModel> itemsModelList;
    private String listName;

    public HomePageModel(int TYPE, List<ItemsModel> itemsModelList, String listName) {
        this.TYPE = TYPE;
        this.itemsModelList = itemsModelList;
        this.listName = listName;
    }
    public static int getFinishWorkListView() {
        return FINISH_WORK_LIST_VIEW;
    }
    public List<ItemsModel> getItemsModelList() {
        return itemsModelList;
    }
    public void setItemsModelList(List<ItemsModel> itemsModelList) {
        this.itemsModelList = itemsModelList;
    }
    public String getListName() {
        return listName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    //////////////////////Items List layouts
}
