package com.steiner.app.DataBase;


import com.steiner.app.Fragment.Contact.MsgModel;
import com.steiner.app.Models.Category.CategoryModel;
import com.steiner.app.Models.Image.GalleryModel;
import com.steiner.app.Models.Items.ItemsModel;
import com.steiner.app.Models.Notification.NotificationModel;
import com.steiner.app.Models.UserModel;
import com.steiner.app.Slider.SlideModel;

import java.util.ArrayList;

public interface DatabaseInterface {

    boolean clearAll();

    boolean clearCategoryData();
    boolean addCategoryData(CategoryModel categoryModel);
    ArrayList<CategoryModel> getCategoryData();
    ArrayList<CategoryModel> getCategoryDataWithId(String id);

    boolean clearSlideData();
    boolean addSlideData(SlideModel slideModel);
    ArrayList<SlideModel> getSlideData();

    boolean clearItemsData();
    boolean addItemsData(ItemsModel itemsModel);
    ArrayList<ItemsModel> getItemsData();
    ArrayList<ItemsModel> getItemsDataWithCategory(String CATEGORY);
    ArrayList<ItemsModel> getItemsDataWithId(String ID);
    boolean chalkItemsDataWithId(String id);
    boolean clearItemsDataWithId(String id);

    boolean clearMessageData();
    boolean addMessageData(MsgModel msgModel);
    boolean getMessageData(String MsgId);
    ArrayList<MsgModel> getMessageData();
    ArrayList<MsgModel> getMessageDataWithId(String MsgId);

    boolean clearUserData();
    boolean addUserData(UserModel userModel);
    ArrayList<UserModel> getUserData();
    ArrayList<UserModel> getUserDataWithId(String UserId);
    boolean updateUserDateData(String phone, String PhoneId, String UserId);

    boolean clearGalleryData();
    boolean addGalleryData(GalleryModel galleryModel);
    ArrayList<GalleryModel> getGalleryData();
    ArrayList<GalleryModel> getGalleryDataWithCategoryId(String id);
    ArrayList<GalleryModel> getGalleryDataWithId(String id);
    boolean chalkGalleryDataWithId(String id);
    boolean clearGalleryDataWithId(String id);

    boolean clearFavImgData();
    boolean addFavImgData(String s);
    ArrayList<String> getFavImgData();
    ArrayList<String> getFavImgDataWithId(String id);
    boolean chalkFavImgDataWithId(String id);
    boolean deleteFavImgDataWithId(String id);

    boolean clearFavItemData();
    boolean addFavItemData(String s);
    ArrayList<String> getFavItemData();
    ArrayList<String> getFavItemDataWithId(String id);
    boolean chalkFavItemDataWithId(String id);
    boolean deleteFavItemDataWithId(String id);

    boolean clearNotificationData();
    boolean addNotificationData(NotificationModel notificationModel);
    boolean getNotificationData(String MsgId);
    ArrayList<NotificationModel> getNotificationData();
}
