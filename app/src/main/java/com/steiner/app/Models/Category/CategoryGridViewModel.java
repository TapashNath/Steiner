package com.steiner.app.Models.Category;

public class CategoryGridViewModel {
    String id;
    String Image;
    String Name;

    public CategoryGridViewModel(String id, String image, String name) {
        this.id = id;
        Image = image;
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

