package com.steiner.app.Fragment.Contact;

public class MsgModel {
    private String id, userId, message, time, date, show;

    public MsgModel(String id, String userId, String message, String time, String date, String show) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.time = time;
        this.date = date;
        this.show = show;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getShow() {
        return show;
    }
    public void setShow(String show) {
        this.show = show;
    }
}
