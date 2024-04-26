package com.steiner.app.Fragment.Contact;

public class ContactModel {

    public static final int SENDER_MSG = 1;
    public static final int SENDER_IMAGE_MSG = 2;
    public static final int SENDER_WORK_MSG = 3;
    public static final int SENDER_REPLY_MSG = 4;

    public static final int RECEIVER_MSG = -1;
    public static final int RECEIVER_IMAGE_MSG = -2;
    public static final int RECEIVER_WORK_MSG = -3;
    public static final int RECEIVER_REPLY_MSG = 14;

    public static final int SENDER_ITEM_INQUIRY = 3;
    public static final int RECEIVER_ITEM_INQUIRY = -3;

    public static final int DATE = 22;
    public int TYPE;

    public String id, userId, message, itemId, replyId, image, title, details, time, date, show;


    public ContactModel(int TYPE, String id, String userId, String message, String time, String show) {
        this.TYPE = TYPE;
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.time = time;
        this.show = show;
    }

    public ContactModel(int TYPE, String id, String userId, String message, String itemId, String replyId, String image, String time, String show) {
        this.TYPE = TYPE;
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.itemId = itemId;
        this.replyId = replyId;
        this.image = image;
        this.time = time;
        this.show = show;
    }

    public ContactModel(int TYPE, String id, String userId, String message, String itemId, String replyId, String image, String title, String details, String time, String show) {
        this.TYPE = TYPE;
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.itemId = itemId;
        this.replyId = replyId;
        this.image = image;
        this.title = title;
        this.details = details;
        this.time = time;
        this.show = show;
    }

    public ContactModel(int TYPE, String id, String userId, String message, String replyId, String image, String time, String show) {
        this.TYPE = TYPE;
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.replyId = replyId;
        this.image = image;
        this.time = time;
        this.show = show;
    }

    public ContactModel(int TYPE, String id, String date) {
        this.TYPE = TYPE;
        this.id = id;
        this.date = date;
    }



    public static int getSenderReplyMsg() {
        return SENDER_REPLY_MSG;
    }

    public static int getReceiverReplyMsg() {
        return RECEIVER_REPLY_MSG;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public static int getSenderMsg() {
        return SENDER_MSG;
    }

    public static int getSenderImageMsg() {
        return SENDER_IMAGE_MSG;
    }

    public static int getSenderWorkMsg() {
        return SENDER_WORK_MSG;
    }

    public static int getReceiverMsg() {
        return RECEIVER_MSG;
    }

    public static int getReceiverImageMsg() {
        return RECEIVER_IMAGE_MSG;
    }

    public static int getReceiverWorkMsg() {
        return RECEIVER_WORK_MSG;
    }

    public static int getSenderItemInquiry() {
        return SENDER_ITEM_INQUIRY;
    }

    public static int getReceiverItemInquiry() {
        return RECEIVER_ITEM_INQUIRY;
    }

    public static int getDATE() {
        return DATE;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
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
