package com.steiner.app.Models.Category;


public class CategoryModel {
    private int id;
    private String CategoryId;
    private String CategoryImage;
    private String CategoryTitle;

    public CategoryModel(String categoryId, String categoryImage, String categoryTitle) {

        CategoryId = categoryId;
        CategoryImage = categoryImage;
        CategoryTitle = categoryTitle;
    }

    public CategoryModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        CategoryTitle = categoryTitle;
    }


}

