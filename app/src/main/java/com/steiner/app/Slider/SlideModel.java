package com.steiner.app.Slider;


public class SlideModel {
    private String id;
    private String image;
    private String link;
    private String key;


    public SlideModel(String id, String image, String link, String key) {
        this.id = id;
        this.image = image;
        this.link = link;
        this.key = key;
    }

    public SlideModel(String image, String link, String key) {
        this.image = image;
        this.link = link;
        this.key = key;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
