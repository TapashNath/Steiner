package com.steiner.app.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Fragment.Contact.MsgModel;
import com.steiner.app.Models.Category.CategoryModel;
import com.steiner.app.Models.Image.GalleryModel;
import com.steiner.app.Models.Notification.NotificationModel;
import com.steiner.app.Models.UserModel;
import com.steiner.app.R;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.VolleyService.IResult;
import com.steiner.app.VolleyService.VolleyService;
import com.steiner.app.databinding.ActivitySplashBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.Content;
import com.steiner.app.utils.NetWorkChaker.InternetReceiver;
import com.steiner.app.utils.NetWorkChaker.NetworkUtils;
import com.steiner.app.utils.SPDataSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding splashBinding;
    private Handler handler;
    private Runnable runnable;
    private SPDataSave spDataSave;
    private DBHandler dbHandler;
    private FirebaseAuth firebaseAuth;
    private BroadcastReceiver InterNetReceiver;

    //    private ProgressDialogView progressDialogView;
    private IResult mResultCallback = null;
    private VolleyService mVolleyService;
    private String versionName;
    private AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getApplicationContext());
        spDataSave = new SPDataSave();
        handler = new Handler();
        dbHandler = new DBHandler();
        builder1 = new AlertDialog.Builder(this);






        InterNetReceiver = new InternetReceiver();
        brodCastReceiver();
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Config.Log("hash  " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }
    }

    private void brodCastReceiver() {
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        splashBinding.VarsonCode.setText("Version : " + versionName);

        registerReceiver(InterNetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        String status = NetworkUtils.getConnectivityStatus(getApplicationContext());
        if (status == null) {
            status = "NoInternet";
        }
        Config.Log(status);
        if (!status.equals("NoInternet") && !status.equals("NoInternetAvailable")) {
            if (firebaseAuth.getCurrentUser() != null) {
                GetAllAfterLoinData();
            } else {
                GetAllData();
            }


            FirebaseInstallations firebaseInstallations = FirebaseInstallations.getInstance();
            firebaseInstallations.getId().addOnCompleteListener(task -> Config.Log(task.getResult()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(InterNetReceiver);
    }


    private void GetAllAfterLoinData() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Config.Log( "tt "+token);
                String  email = firebaseAuth.getCurrentUser().getEmail();
                HashMap<String, String> prams = new HashMap<>();
                prams.put("email", email);
                prams.put("token", token);
                Config.Log(email);
                mVolleyService.CallDataVolley("GetAllAfterLoinData", "Steiner/getUserDataAfterLogin.php", prams);
            } else {
                Config.Log("token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task -> Config.Log(""));

    }

    private void GetAllData() {
        mVolleyService.GetDataVolley("GetAllData", "Steiner/GetUserData.php");
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void notifySuccess(String requestType, String response) {
                if ("GetAllData".equals(requestType)) {
                    Config.Log(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_slides = obj.getString("response_slides");
                        switch (response_slides) {
                            case "1":
                                dbHandler.clearSlideData();
                                JSONArray response_slides_data = obj.getJSONArray("response_slides_data");
                                for (int i = 0; i < response_slides_data.length(); i++) {
                                    JSONObject jsonDataObject = response_slides_data.getJSONObject(i);
                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String link = jsonDataObject.getString("link");


                                    dbHandler.addSlideData(new SlideModel(id, image, link, ""));
                                }
                                Config.Log("slide data get complete");
                                break;
                            case "2":
                                dbHandler.clearSlideData();
                                Config.Log("slide data get error");
                                break;
                        }

                        String response_categories = obj.getString("response_categories");
                        switch (response_categories) {
                            case "1":
                                dbHandler.clearCategoryData();
                                JSONArray response_categories_data = obj.getJSONArray("response_categories_data");
                                for (int i = 0; i < response_categories_data.length(); i++) {
                                    JSONObject jsonDataObject = response_categories_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String title = jsonDataObject.getString("name");

                                    dbHandler.addCategoryData(new CategoryModel(id, image, title));
                                }
                                Config.Log("category data get complete");
                                break;
                            case "2":
                                dbHandler.clearCategoryData();
                                Config.Log("category data get error");
                                break;
                        }

                        String response_images = obj.getString("response_images");
                        switch (response_images) {
                            case "1": {
                                dbHandler.clearGalleryData();
                                JSONArray response_images_data = obj.getJSONArray("response_images_data");
                                for (int i = 0; i < response_images_data.length(); i++) {
                                    JSONObject jsonDataObject = response_images_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String categories_id = jsonDataObject.getString("categories_id");

                                    dbHandler.addGalleryData(new GalleryModel(id, image, categories_id));

                                }
                                Config.Log("image data get complete");
                                break;
                            }
                            case "2":
                                dbHandler.clearItemsData();
                                Config.Log("items data get error");
                                break;

                        }


                        if (getIntent() != null) {
                            getDynamicLink();
                        } else {
                            startApp();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        builder1.setTitle("Alert Message");
                        builder1.setIcon(android.R.drawable.stat_notify_error);
                        builder1.setMessage("Database error !");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Try Again",
                                (dialog, id) -> {
                                    GetAllData();
                                });

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
//
                    }
                }
                if ("GetAllAfterLoinData".equals(requestType)) {
                    Config.Log(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_slides = obj.getString("response_slides");
                        switch (response_slides) {
                            case "1":
                                dbHandler.clearSlideData();
                                JSONArray response_slides_data = obj.getJSONArray("response_slides_data");
                                for (int i = 0; i < response_slides_data.length(); i++) {
                                    JSONObject jsonDataObject = response_slides_data.getJSONObject(i);
                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String link = jsonDataObject.getString("link");


                                    dbHandler.addSlideData(new SlideModel(id, image, link, ""));
                                }
                                Config.Log("slide data get complete");
                                break;
                            case "2":
                                dbHandler.clearSlideData();
                                Config.Log("slide data get error");
                                break;
                        }

                        String response_categories = obj.getString("response_categories");
                        switch (response_categories) {
                            case "1":
                                dbHandler.clearCategoryData();
                                JSONArray response_categories_data = obj.getJSONArray("response_categories_data");
                                for (int i = 0; i < response_categories_data.length(); i++) {
                                    JSONObject jsonDataObject = response_categories_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String title = jsonDataObject.getString("name");

                                    dbHandler.addCategoryData(new CategoryModel(id, image, title));
                                }
                                Config.Log("category data get complete");
                                break;
                            case "2":
                                dbHandler.clearCategoryData();
                                Config.Log("category data get error");
                                break;
                        }

                        String response_images = obj.getString("response_images");
                        switch (response_images) {
                            case "1": {
                                dbHandler.clearGalleryData();
                                JSONArray response_images_data = obj.getJSONArray("response_images_data");
                                for (int i = 0; i < response_images_data.length(); i++) {
                                    JSONObject jsonDataObject = response_images_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String categories_id = jsonDataObject.getString("categories_id");

                                    dbHandler.addGalleryData(new GalleryModel(id, image, categories_id));

                                }
                                Config.Log("image data get complete");
                                break;
                            }
                            case "2":
                                dbHandler.clearItemsData();
                                Config.Log("items data get error");
                                break;

                        }


                        String response_user = obj.getString("response_user");
                        switch (response_user) {
                            case "2": {
                                dbHandler.clearUserData();
                                JSONArray response_user_data = obj.getJSONArray("response_user_data");
                                JSONObject jsonDataObject = response_user_data.getJSONObject(0);

                                String type = jsonDataObject.getString("type");
                                String username = jsonDataObject.getString("username");
                                String image = jsonDataObject.getString("image");
                                String email = jsonDataObject.getString("email");
                                String token = jsonDataObject.getString("token");



                                dbHandler.addUserData(new UserModel(type,username,email,image,token));
                                Log.d("LOGIN_ACTIVITY", "gating user data complete");
                                Config.showLongToast("WellCome Back " + username);
                                break;
                            }

                            case "1":
                                dbHandler.clearUserData();
                                signOut();
                                Log.d("LOGIN_ACTIVITY", "gating user data error ");
                                break;
                        }

                        String response_message = obj.getString("response_message");
                        switch (response_message) {
                            case "1":
                                dbHandler.clearMessageData();
                                JSONArray response_message_data = obj.getJSONArray("response_message_data");
                                for (int i = 0; i < response_message_data.length(); i++) {
                                    JSONObject jsonDataObject = response_message_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String userId = jsonDataObject.getString("email");
                                    String message = jsonDataObject.getString("message");
                                    String time = jsonDataObject.getString("time");
                                    String date = jsonDataObject.getString("date");
                                    String show = jsonDataObject.getString("show");

                                    dbHandler.addMessageData(new MsgModel(id, userId, message, time, date, show));
                                }
                                Config.Log("message data get complete");
                                break;
                            case "2":
                                dbHandler.clearMessageData();
                                Config.Log("message data get error");
                                break;
                            case "3":
                                dbHandler.clearMessageData();
                                Config.Log("message data error");
                                break;
                        }

                        String response_notification = obj.getString("response_notification");
                        switch (response_notification) {
                            case "1":
                                dbHandler.clearNotificationData();
                                JSONArray response_notification_data = obj.getJSONArray("response_notification_data");
                                for (int i = 0; i < response_notification_data.length(); i++) {
                                    JSONObject jsonDataObject = response_notification_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String UserId = jsonDataObject.getString("email");
                                    String msg = jsonDataObject.getString("msg");
                                    String image = jsonDataObject.getString("image");

                                    dbHandler.addNotificationData(new NotificationModel(id, msg, image));
                                }
                                Config.Log("notification data get complete");
                                break;
                            case "2":
                                dbHandler.clearNotificationData();
                                Config.Log("notification data get error");
                                break;
                        }

                        if (getIntent() != null) {
                            getDynamicLink();
                        } else {
                            startApp();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        builder1.setTitle("Alert Message");
                        builder1.setIcon(android.R.drawable.stat_notify_error);
                        builder1.setMessage("Database error !");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Try Again",
                                (dialog, id) -> {
                                    GetAllAfterLoinData();
                                });

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                builder1.setTitle("Alert Message");
                builder1.setIcon(android.R.drawable.stat_notify_error);
                builder1.setMessage("Network error !");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Try Again",
                        (dialog, id) -> {
                            GetAllData();
                        });
                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        };
    }


    private void signOut() {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
                    Log.d("APP_ACTIVITY", "sing out");
                    FirebaseAuth.getInstance().signOut();
                    boolean l = dbHandler.clearAll();
                    if (l) {
                       GetAllData();
                    }
                });
    }

    private void startApp() {
        runnable = () -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        };
        handler.postDelayed(runnable, 3000);
    }

    private void getDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                    }
                    Config.Log(String.valueOf(deepLink));
                    if (deepLink != null) {
                        String category = deepLink.getQueryParameter("category");
                        String id = deepLink.getQueryParameter("itemId");

                        if (category.equals(Content.IMAGE_DETAILS)) {
                            spDataSave.setStr("SELECT_IMAGE", "ID", id);
                            spDataSave.setBool("SELECT_IMAGE", "RE", true);
                            startApp();
                        }

                    } else {
                        startApp();
                    }

                })
                .addOnFailureListener(this, e -> Config.Log("getDynamicLink:onFailure" + e.getMessage()));
    }
}