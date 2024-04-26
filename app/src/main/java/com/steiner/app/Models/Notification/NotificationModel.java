package com.steiner.app.Models.Notification;

public class NotificationModel {
    String id, image, msg;

    public NotificationModel(String id, String image, String msg) {
        this.id = id;
        this.image = image;
        this.msg = msg;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
