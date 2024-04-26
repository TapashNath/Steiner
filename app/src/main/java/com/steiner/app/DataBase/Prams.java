package com.steiner.app.DataBase;

public class Prams {

    public static final String DB_NAME = "appName";
    public static final int DB_VERSION = 1;

    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String DEFAULT_ID_COL = "id";
    public static final String CATEGORY_ID_COL = "cat_id";
    public static final String CATEGORY_IMAGE_COL = "cat_image";
    public static final String CATEGORY_TITLE_COL = "cat_title";

    public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + Prams.CATEGORY_TABLE_NAME + " ("
            + Prams.DEFAULT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.CATEGORY_ID_COL + " TEXT,"
            + Prams.CATEGORY_IMAGE_COL + " TEXT,"
            + Prams.CATEGORY_TITLE_COL + " TEXT)";

    public static final String SERVICE_TABLE_NAME = "service_table";
    public static final String SERVICE_DEFAULT_ID = "Id";
    public static final String SERVICE_IMAGE = "image";
    public static final String SERVICE_TITLE = "title";
    public static final String SERVICE_SUB_TITLE = "sub_title";
    public static final String SERVICE_PRICE = "price";

    public static final String CREATE_SERVICE_TABLE = "CREATE TABLE " + Prams.SERVICE_TABLE_NAME + " ("
            + Prams.SERVICE_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.SERVICE_IMAGE + " TEXT,"
            + Prams.SERVICE_TITLE + " TEXT,"
            + Prams.SERVICE_SUB_TITLE + " TEXT,"
            + Prams.SERVICE_PRICE + " TEXT)";

    public static final String SLIDE_TABLE_NAME = "slide";
    public static final String SLIDE_DEFAULT_ID = "id";
    public static final String SLIDE_ID_COL = "slide_id";
    public static final String SLIDE_IMAGE_COL = "image";
    public static final String SLIDE_LINK_COL = "link";
    public static final String SLIDE_KEY_COL = "key";

    public static final String CREATE_SLIDE_TABLE = "CREATE TABLE " + Prams.SLIDE_TABLE_NAME + " ("
            + Prams.SLIDE_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.SLIDE_ID_COL + " TEXT,"
            + Prams.SLIDE_IMAGE_COL + " TEXT,"
            + Prams.SLIDE_LINK_COL + " TEXT,"
            + Prams.SLIDE_KEY_COL + " TEXT)";

    public static final String ITEMS_TABLE_NAME = "items";
    public static final String ITEMS_DEFAULT_ID = "id";
    public static final String ITEMS_ID_COL = "item_id";
    public static final String ITEMS_IMAGES = "image";
    public static final String ITEMS_CATEGORY = "cate";
    public static final String ITEMS_TITLE = "title";
    public static final String ITEMS_DETAILS = "details";
    public static final String ITEMS_LIKE = "like";
    public static final String ITEMS_SHARE = "share";

    public static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + Prams.ITEMS_TABLE_NAME + " ("
            + Prams.ITEMS_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.ITEMS_ID_COL +" TEXT,"
            + Prams.ITEMS_IMAGES + " TEXT,"
            + Prams.ITEMS_CATEGORY + " TEXT,"
            + Prams.ITEMS_TITLE + " TEXT,"
            + Prams.ITEMS_DETAILS + " TEXT,"
            + Prams.ITEMS_LIKE + " TEXT,"
            + Prams.ITEMS_SHARE + " TEXT)";


    public static final String MSG_TABLE_NAME = "contact";
    public static final String MSG_DEFAULT_ID = "def_id";
    public static final String ID = "id";
    public static final String MSG_ID = "msg_id";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG = "message";
    public static final String MSG_TIME = "time";
    public static final String MSG_DATE = "date";
    public static final String MSG_SHOW = "show";

    public static final String CREATE_MSG_TABLE = "CREATE TABLE " + Prams.MSG_TABLE_NAME + " ("
            + Prams.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.MSG_DEFAULT_ID + " TEXT,"
            + Prams.MSG_ID + " TEXT,"
            + Prams.MSG_USER_ID + " TEXT,"
            + Prams.MSG + " TEXT,"
            + Prams.MSG_TIME + " TEXT,"
            + Prams.MSG_DATE + " TEXT,"
            + Prams.MSG_SHOW + " TEXT)";


    public static final String USER_TABLE_NAME = "user";
    public static final String USER_DEFAULT_ID = "default_id";
    public static final String USER_TYPE = "id";
    public static final String USER_USERNAME = "username";
    public static final String USER_EMAIL = "email";
    public static final String USER_IMAGE = "image";
    public static final String USER_TOKEN = "token";

    public static final String CREATE_USER_TABLE = "CREATE TABLE " + Prams.USER_TABLE_NAME + " ("
            + Prams.USER_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.USER_TYPE + " TEXT,"
            + Prams.USER_USERNAME + " TEXT,"
            + Prams.USER_EMAIL + " TEXT,"
            + Prams.USER_IMAGE + " TEXT,"
            + Prams.USER_TOKEN + " TEXT)";

    public static final String GALLERY_TABLE_NAME = "gallery";
    public static final String GALLERY_DEFAULT_ID = "default_id";
    public static final String GALLERY_ID = "id";
    public static final String GALLERY_IMAGE = "image";
    public static final String GALLERY_CATEGORY_ID = "category_id";

    public static final String CREATE_GALLERY_TABLE = "CREATE TABLE " + Prams.GALLERY_TABLE_NAME + " ("
            + Prams.GALLERY_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.GALLERY_ID + " TEXT,"
            + Prams.GALLERY_IMAGE + " TEXT,"
            + Prams.GALLERY_CATEGORY_ID + " TEXT)";

    public static final String USER_FAV_IMAGE_TABLE_NAME = "fav_image";
    public static final String USER_FAV_IMAGE_DEFAULT_ID = "default_id";
    public static final String USER_FAV_IMAGE_ID = "id";

    public static final String CREATE_USER_FAV_IMAGE_TABLE = "CREATE TABLE " + Prams.USER_FAV_IMAGE_TABLE_NAME + " ("
            + Prams.USER_FAV_IMAGE_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.USER_FAV_IMAGE_ID + " TEXT)";

    public static final String USER_FAV_ITEM_TABLE_NAME = "fav_item";
    public static final String USER_FAV_ITEM_DEFAULT_ID = "default_id";
    public static final String USER_FAV_ITEM_ID = "id";

    public static final String CREATE_USER_FAV_ITEM_TABLE = "CREATE TABLE " + Prams.USER_FAV_ITEM_TABLE_NAME + " ("
            + Prams.USER_FAV_ITEM_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.USER_FAV_ITEM_ID + " TEXT)";


    public static final String NOTIFICATION_TABLE_NAME = "notification";
    public static final String NOTIFICATION_DEFAULT_ID = "default_id";
    public static final String NOTIFICATION_ID = "id";
    public static final String NOTIFICATION_IMAGE = "image";
    public static final String NOTIFICATION_MSG = "msg";

    public static final String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + Prams.NOTIFICATION_TABLE_NAME + " ("
            + Prams.NOTIFICATION_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Prams.NOTIFICATION_ID + " TEXT,"
            + Prams.NOTIFICATION_IMAGE + " TEXT,"
            + Prams.NOTIFICATION_MSG + " TEXT)";

}