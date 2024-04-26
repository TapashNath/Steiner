package com.steiner.app.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.steiner.app.Fragment.Contact.MsgModel;
import com.steiner.app.Models.Category.CategoryModel;
import com.steiner.app.Models.Image.GalleryModel;
import com.steiner.app.Models.Items.ItemsModel;
import com.steiner.app.Models.Notification.NotificationModel;
import com.steiner.app.Models.UserModel;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.utils.MyApplication;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper implements DatabaseInterface {

    public DBHandler() {
        super(MyApplication.context, Prams.DB_NAME, null, Prams.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Prams.CREATE_SERVICE_TABLE);
        db.execSQL(Prams.CREATE_SLIDE_TABLE);
        db.execSQL(Prams.CREATE_CATEGORY_TABLE);
        db.execSQL(Prams.CREATE_ITEMS_TABLE);
        db.execSQL(Prams.CREATE_MSG_TABLE);
        db.execSQL(Prams.CREATE_USER_TABLE);
        db.execSQL(Prams.CREATE_GALLERY_TABLE);
        db.execSQL(Prams.CREATE_USER_FAV_IMAGE_TABLE);
        db.execSQL(Prams.CREATE_USER_FAV_ITEM_TABLE);
        db.execSQL(Prams.CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Prams.SERVICE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.SLIDE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.ITEMS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.MSG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.GALLERY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.USER_FAV_IMAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.USER_FAV_ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Prams.NOTIFICATION_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public boolean clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.SLIDE_TABLE_NAME);
        db.execSQL("delete from " + Prams.SERVICE_TABLE_NAME);
        db.execSQL("delete from " + Prams.CATEGORY_TABLE_NAME);
        db.execSQL("delete from " + Prams.ITEMS_TABLE_NAME);
        db.execSQL("delete from " + Prams.MSG_TABLE_NAME);
        db.execSQL("delete from " + Prams.USER_TABLE_NAME);
        db.execSQL("delete from " + Prams.GALLERY_TABLE_NAME);
        db.execSQL("delete from " + Prams.USER_FAV_IMAGE_TABLE_NAME);
        db.execSQL("delete from " + Prams.USER_FAV_ITEM_TABLE_NAME);
        db.execSQL("delete from " + Prams.NOTIFICATION_TABLE_NAME);
        return true;
    }

    @Override
    public boolean clearSlideData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.SLIDE_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addSlideData(SlideModel slideModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Prams.SLIDE_ID_COL, slideModel.getId());
        values.put(Prams.SLIDE_IMAGE_COL, slideModel.getImage());
        values.put(Prams.SLIDE_LINK_COL, slideModel.getLink());
        values.put(Prams.SLIDE_KEY_COL, slideModel.getKey());

        long i = db.insert(Prams.SLIDE_TABLE_NAME, null, values);
        db.close();
        return i > 0;
    }

    @Override
    public ArrayList<SlideModel> getSlideData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.SLIDE_TABLE_NAME, null);

        ArrayList<SlideModel> slideModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                slideModels.add(new SlideModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.SLIDE_ID_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.SLIDE_IMAGE_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.SLIDE_LINK_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.SLIDE_KEY_COL))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return slideModels;
    }

    @Override
    public boolean clearItemsData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.ITEMS_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addItemsData(ItemsModel itemsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Prams.ITEMS_ID_COL, itemsModel.getId());
        values.put(Prams.ITEMS_IMAGES, itemsModel.getImages());
        values.put(Prams.ITEMS_CATEGORY, itemsModel.getCategory());
        values.put(Prams.ITEMS_TITLE, itemsModel.getTitle());
        values.put(Prams.ITEMS_DETAILS, itemsModel.getDetails());
        values.put(Prams.ITEMS_LIKE, itemsModel.getLike());
        values.put(Prams.ITEMS_SHARE, itemsModel.getShare());

        long i = db.insert(Prams.ITEMS_TABLE_NAME, null, values);
        db.close();
        return i > 0;
    }

    @Override
    public ArrayList<ItemsModel> getItemsData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.ITEMS_TABLE_NAME, null);

        ArrayList<ItemsModel> itemsModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                itemsModels.add(new ItemsModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_ID_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_IMAGES)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_CATEGORY)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_TITLE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_DETAILS)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_LIKE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_SHARE))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return itemsModels;
    }

    @Override
    public ArrayList<ItemsModel> getItemsDataWithCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.ITEMS_TABLE_NAME + " WHERE " + Prams.ITEMS_CATEGORY + " = " + "'" + category + "' ", null);
        ArrayList<ItemsModel> itemsModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                itemsModels.add(new ItemsModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_ID_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_IMAGES)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_CATEGORY)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_TITLE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_DETAILS)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_LIKE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_SHARE))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return itemsModels;
    }

    @Override
    public ArrayList<ItemsModel> getItemsDataWithId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.ITEMS_TABLE_NAME + " WHERE " + Prams.ITEMS_ID_COL + " = " + "'" + id + "' ", null);
        ArrayList<ItemsModel> itemsModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                itemsModels.add(new ItemsModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_ID_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_IMAGES)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_CATEGORY)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_TITLE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_DETAILS)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_LIKE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.ITEMS_SHARE))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return itemsModels;
    }

    @Override
    public boolean chalkItemsDataWithId(String id) {
        String query = "SELECT * FROM " + Prams.ITEMS_TABLE_NAME + " WHERE " + Prams.ITEMS_ID_COL + " = ?";
        String[] whereArgs = {id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }

    @Override
    public boolean clearItemsDataWithId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + Prams.ITEMS_TABLE_NAME + " WHERE " + Prams.ITEMS_ID_COL + " = ?";
        String[] whereArgs = {id};
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }

    /////////////////CATEGORY///////////////////////////
    @Override
    public boolean clearCategoryData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.CATEGORY_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addCategoryData(CategoryModel categoryModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Prams.CATEGORY_ID_COL, categoryModel.getCategoryId());
        values.put(Prams.CATEGORY_IMAGE_COL, categoryModel.getCategoryImage());
        values.put(Prams.CATEGORY_TITLE_COL, categoryModel.getCategoryTitle());

        long i = db.insert(Prams.CATEGORY_TABLE_NAME, null, values);
        db.close();
        return i > 0;
    }

    @Override
    public ArrayList<CategoryModel> getCategoryData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.CATEGORY_TABLE_NAME, null);

        ArrayList<CategoryModel> categoryModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                categoryModels.add(new CategoryModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.CATEGORY_ID_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.CATEGORY_IMAGE_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.CATEGORY_TITLE_COL))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return categoryModels;
    }


    @Override
    public ArrayList<CategoryModel> getCategoryDataWithId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.CATEGORY_TABLE_NAME + " WHERE " + Prams.CATEGORY_ID_COL + " = " + "'" + id + "' ", null);
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                categoryModels.add(new CategoryModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.CATEGORY_ID_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.CATEGORY_IMAGE_COL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.CATEGORY_TITLE_COL))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return categoryModels;
    }

    @Override
    public boolean clearMessageData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.MSG_TABLE_NAME);
        db.close();
        return true;
    }


    @Override
    public boolean addMessageData(MsgModel msgModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prams.MSG_ID, msgModel.getId());
        values.put(Prams.MSG_USER_ID, msgModel.getUserId());
        values.put(Prams.MSG, msgModel.getMessage());
        values.put(Prams.MSG_TIME, msgModel.getTime());
        values.put(Prams.MSG_DATE, msgModel.getDate());
        values.put(Prams.MSG_SHOW, msgModel.getShow());

        long i = db.insert(Prams.MSG_TABLE_NAME, null, values);
        db.close();
        return i > 0;
    }

    @Override
    public boolean getMessageData(String MsgId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.MSG_TABLE_NAME + " WHERE " + Prams.MSG_ID + " = " + "'" + MsgId + "' ", null);
        ArrayList<MsgModel> msgModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                msgModels.add(new MsgModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_USER_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_TIME)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_DATE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_SHOW))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return msgModels.size() > 0;
    }


    @Override
    public ArrayList<MsgModel> getMessageData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.MSG_TABLE_NAME, null);
        ArrayList<MsgModel> msgModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                msgModels.add(new MsgModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_USER_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_TIME)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_DATE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_SHOW))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return msgModels;
    }

    @Override
    public ArrayList<MsgModel> getMessageDataWithId(String MsgId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.MSG_TABLE_NAME + " WHERE " + Prams.MSG_ID + " = " + "'" + MsgId + "' ", null);
        ArrayList<MsgModel> msgModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                msgModels.add(new MsgModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_USER_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_TIME)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_DATE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.MSG_SHOW))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return msgModels ;
    }


    /////////////////CATEGORY///////////////////////////

    /////////////////USER///////////////////////////
    @Override
    public boolean clearUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.USER_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addUserData(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prams.USER_TYPE, userModel.getType());
        values.put(Prams.USER_USERNAME, userModel.getUsername());
        values.put(Prams.USER_EMAIL, userModel.getEmail());
        values.put(Prams.USER_IMAGE, userModel.getImage());
        values.put(Prams.USER_TOKEN, userModel.getToken());


        long i = db.insert(Prams.USER_TABLE_NAME, null, values);

        db.close();

        return i > 0;
    }

    @Override
    public ArrayList<UserModel> getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.USER_TABLE_NAME, null);

        ArrayList<UserModel> userModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                userModels.add(new UserModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_TYPE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_USERNAME)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_EMAIL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_IMAGE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_TOKEN))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return userModels;
    }

    @Override
    public ArrayList<UserModel> getUserDataWithId(String UserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.USER_TABLE_NAME + " WHERE " + Prams.USER_TYPE + " = " + "'" + UserId + "' ", null);
        ArrayList<UserModel> userModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                userModels.add(new UserModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_TYPE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_USERNAME)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_EMAIL)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_IMAGE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_TOKEN))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return userModels;
    }

    @Override
    public boolean updateUserDateData(String phone, String PhoneId, String UserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = 1;
        return true;
    }

    @Override
    public boolean clearGalleryData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.GALLERY_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addGalleryData(GalleryModel galleryModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prams.GALLERY_ID, galleryModel.getId());
        values.put(Prams.GALLERY_IMAGE, galleryModel.getImage());
        values.put(Prams.GALLERY_CATEGORY_ID, galleryModel.getCategory_id());


        long i = db.insert(Prams.GALLERY_TABLE_NAME, null, values);

        db.close();
        return i > 0;
    }

    @Override
    public ArrayList<GalleryModel> getGalleryData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.GALLERY_TABLE_NAME, null);
        ArrayList<GalleryModel> galleryModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                galleryModels.add(new GalleryModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_IMAGE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_CATEGORY_ID))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return galleryModels;
    }

    @Override
    public ArrayList<GalleryModel> getGalleryDataWithCategoryId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.GALLERY_TABLE_NAME + " WHERE " + Prams.GALLERY_CATEGORY_ID + " = " + "'" + id + "' ORDER BY " + Prams.GALLERY_ID +" DESC ", null);
        ArrayList<GalleryModel> galleryModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                galleryModels.add(new GalleryModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_IMAGE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_CATEGORY_ID))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return galleryModels;
    }

    @Override
    public ArrayList<GalleryModel> getGalleryDataWithId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.GALLERY_TABLE_NAME + " WHERE " + Prams.GALLERY_ID + " = " + "'" + id + "'  ORDER BY " + Prams.GALLERY_ID +" DESC", null);
        ArrayList<GalleryModel> galleryModels = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                galleryModels.add(new GalleryModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_IMAGE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.GALLERY_CATEGORY_ID))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return galleryModels;
    }


    @Override
    public boolean chalkGalleryDataWithId(String id) {
        String query = "SELECT * FROM " + Prams.GALLERY_TABLE_NAME + " WHERE " + Prams.GALLERY_ID + " = ?";
        String[] whereArgs = {id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }

    @Override
    public boolean clearGalleryDataWithId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + Prams.GALLERY_TABLE_NAME + " WHERE " + Prams.GALLERY_ID + " = ?";
        String[] whereArgs = {id};
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }


    @Override
    public boolean clearFavImgData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.USER_FAV_IMAGE_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addFavImgData(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prams.USER_FAV_IMAGE_ID, s);
        long i = db.insert(Prams.USER_FAV_IMAGE_TABLE_NAME, null, values);

        db.close();
        return i > 0;
    }

    @Override
    public ArrayList<String> getFavImgData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.USER_FAV_IMAGE_TABLE_NAME, null);
        ArrayList<String> strings = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                strings.add(cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_FAV_IMAGE_ID)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return strings;
    }

    @Override
    public ArrayList<String> getFavImgDataWithId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.USER_FAV_IMAGE_TABLE_NAME + " WHERE " + Prams.USER_FAV_IMAGE_ID + " = " + "'" + id + "' ", null);
        ArrayList<String> strings = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                strings.add(cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_FAV_IMAGE_ID)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return strings;
    }

    @Override
    public boolean chalkFavImgDataWithId(String id) {
        String query = "SELECT * FROM " + Prams.USER_FAV_IMAGE_TABLE_NAME + " WHERE " + Prams.USER_FAV_IMAGE_ID + " = ?";
        String[] whereArgs = {id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }

    @Override
    public boolean deleteFavImgDataWithId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = Prams.USER_FAV_IMAGE_ID + "=?";
        long s = db.delete(Prams.USER_FAV_IMAGE_TABLE_NAME, where, new String[]{id});

        db.close();
        return s > 0;

    }

    @Override
    public boolean clearFavItemData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.USER_FAV_ITEM_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addFavItemData(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prams.USER_FAV_ITEM_ID, s);
        long i = db.insert(Prams.USER_FAV_ITEM_TABLE_NAME, null, values);

        db.close();
        return i > 0;
    }

    @Override
    public ArrayList<String> getFavItemData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.USER_FAV_ITEM_TABLE_NAME, null);
        ArrayList<String> strings = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                strings.add(cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_FAV_ITEM_ID)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return strings;
    }

    @Override
    public ArrayList<String> getFavItemDataWithId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.USER_FAV_ITEM_TABLE_NAME + " WHERE " + Prams.USER_FAV_IMAGE_ID + " = " + "'" + id + "' ", null);
        ArrayList<String> strings = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                strings.add(cursorCourses.getString(cursorCourses.getColumnIndex(Prams.USER_FAV_ITEM_ID)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return strings;
    }

    @Override
    public boolean chalkFavItemDataWithId(String id) {
        String query = "SELECT * FROM " + Prams.USER_FAV_ITEM_TABLE_NAME + " WHERE " + Prams.USER_FAV_ITEM_ID + " = ?";
        String[] whereArgs = {id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }

    @Override
    public boolean deleteFavItemDataWithId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = Prams.USER_FAV_ITEM_ID + "=?";
        long s = db.delete(Prams.USER_FAV_ITEM_TABLE_NAME, where, new String[]{id});

        db.close();
        return s > 0;
    }

    @Override
    public boolean clearNotificationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Prams.NOTIFICATION_TABLE_NAME);
        db.close();
        return true;
    }

    @Override
    public boolean addNotificationData(NotificationModel notificationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Prams.NOTIFICATION_ID, notificationModel.getId());
        values.put(Prams.NOTIFICATION_IMAGE, notificationModel.getImage());
        values.put(Prams.NOTIFICATION_MSG, notificationModel.getMsg());


        long i = db.insert(Prams.NOTIFICATION_TABLE_NAME, null, values);

        db.close();
        return i > 0;
    }

    @Override
    public boolean getNotificationData(String Id) {
        String query = "SELECT * FROM " + Prams.NOTIFICATION_TABLE_NAME + " WHERE " + Prams.NOTIFICATION_ID + " = ?";
        String[] whereArgs = {Id};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();

        return count >= 1;
    }

    @Override
    public ArrayList<NotificationModel> getNotificationData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Prams.NOTIFICATION_TABLE_NAME, null);
        ArrayList<NotificationModel> notificationModels = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                notificationModels.add(new NotificationModel(
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.NOTIFICATION_ID)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.NOTIFICATION_IMAGE)),
                        cursorCourses.getString(cursorCourses.getColumnIndex(Prams.NOTIFICATION_MSG))));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return notificationModels;
    }


}
