package com.steiner.app.Models.Image;

public class GalleryModel {
    String id, image, category_id;

    public GalleryModel(String id, String image, String category_id) {
        this.id = id;
        this.image = image;
        this.category_id = category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
