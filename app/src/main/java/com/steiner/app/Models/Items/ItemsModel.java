package com.steiner.app.Models.Items;

public class ItemsModel {
    String id, images,category, title, details, like, share;

    public ItemsModel(String id, String images, String category, String title, String details, String like, String share) {
        this.id = id;
        this.images = images;
        this.category = category;
        this.title = title;
        this.details = details;
        this.like = like;
        this.share = share;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public ItemsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
