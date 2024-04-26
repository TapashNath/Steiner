package com.steiner.app.Fragment.Home;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;
import com.steiner.app.Activitys.MainActivity;
import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.BuildConfig;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Fragment.Contact.MsgModel;
import com.steiner.app.Fragment.Service.ServiceFragment;
import com.steiner.app.Fragment.login.LoginInterface;
import com.steiner.app.Fragment.login.SingInFragment;
import com.steiner.app.Models.Category.CategoryGridViewModel;
import com.steiner.app.Models.Category.CategoryModel;
import com.steiner.app.Models.HomePage.HomePageAdapter;
import com.steiner.app.Models.HomePage.HomePageModel;
import com.steiner.app.Models.Image.GalleryModel;
import com.steiner.app.Models.Items.ItemsModel;
import com.steiner.app.Models.Notification.NotificationModel;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.Models.UserModel;
import com.steiner.app.R;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.VolleyService.IResult;
import com.steiner.app.VolleyService.VolleyService;
import com.steiner.app.databinding.FragmentHomeBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.CreateDynamicLink;
import com.steiner.app.utils.NetWorkChaker.NetworkUtils;
import com.steiner.app.utils.SPDataSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnClickListeners, LoginInterface {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private final int REQUEST_CHECK_CODE = 898;

    private LocationSettingsRequest.Builder builder;
    private FragmentHomeBinding fragmentHomeBinding;
    private DBHandler dbHandler;
    private SPDataSave spDataSave;
    private AppCompatActivity activity;
    private OnClickListeners onClickListeners;
    private CreateDynamicLink createDynamicLink;
    private FirebaseAuth firebaseAuth;

    private IResult mResultCallback = null;
    private VolleyService mVolleyService;
    private LoginInterface loginInterface;

    Uri shortLink;

    private HomePageAdapter homePageAdapter;
    private List<HomePageModel> homePageListModelList;
    private List<CategoryGridViewModel> categoryGridViewModels;
    private List<CategoryModel> categoryModels;
    private List<SlideModel> slideModels;
    private List<ItemsModel> itemsModelList;
    private String USER_ID;
    private AlertDialog.Builder builder1;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        dbHandler = new DBHandler();
        firebaseAuth = FirebaseAuth.getInstance();
        spDataSave = new SPDataSave();
        activity = ((AppCompatActivity) requireActivity());
        onClickListeners = this;
        loginInterface = this;
        builder1 = new AlertDialog.Builder(requireContext());
        createDynamicLink = new CreateDynamicLink(activity);

        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, requireContext());

        USER_ID = spDataSave.getStr("USER_DATA", "emailId");
        Config.addToolbarWithNoBack(fragmentHomeBinding.getRoot(), activity, fragmentHomeBinding.MainToolBar.getId(), "");
        inti();


        fragmentHomeBinding.HomePageSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (firebaseAuth.getCurrentUser() != null) {
                    GetAllAfterLoinData();
                } else {
                    GetAllData();
                }
            }
        });

        fragmentHomeBinding.alertShow.setOnClickListener(v -> {
            ServiceFragment bottomSheet = ServiceFragment.newInstance();
            bottomSheet.show(requireFragmentManager(), "ServiceFragment");
        });

        if (!permissionCheck()) {
            requestPermissions();
        } else {
            if (NetworkUtils.isGPSEnable(requireContext())) {
                getAddress();
            } else {
                GPSEnable();
            }
        }

        fragmentHomeBinding.share.setOnClickListener(v -> {
            try {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.whait_name_logo);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), b, "Title", null);
                Uri imageUri = Uri.parse(path);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\n*Let me recommend you this application*\n\nস্বল্প ব্যয়ে নতুন আসবাব দিয়ে আপনার ঘরকে সাজাতে এখুনি এই এপ্লিকেশন টি ডাউনলোড করুন |\n \nDownload this app now to decorate your house with new furniture at low cost and join us forever.\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });

        spDataSave.setBool("APP_DATA", "NOTIFICATION", true);
        FirebaseMessaging.getInstance().subscribeToTopic("all_users").addOnCompleteListener(task -> {
            String msg = getString(R.string.subsribed);
            if (!task.isSuccessful()) {
                msg = getString(R.string.not_subsribed);
            }
        });
        if (firebaseAuth.getCurrentUser() != null) {
            if (!Objects.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(), "debnathtapash56@gmail.com")) {
                AdView adView = new AdView(requireContext());
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId("ca-app-pub-3154311324989962/8880180263");
                MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
                AdRequest adRequest = new AdRequest.Builder().build();
                fragmentHomeBinding.adView.loadAd(adRequest);
            } else {
                fragmentHomeBinding.adView.setVisibility(View.GONE);
            }
        } else {
            AdView adView = new AdView(requireContext());
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId("ca-app-pub-3154311324989962/8880180263");
            MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            fragmentHomeBinding.adView.loadAd(adRequest);
        }


        return fragmentHomeBinding.getRoot();
    }

    private void GPSEnable() {
        Config.Log("GPSEnable");
        LocationRequest request = new LocationRequest()
                .setFastestInterval(3000)
                .setInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder = new LocationSettingsRequest.Builder().addLocationRequest(request);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build());
        result.addOnSuccessListener(task -> getAddress());

        result.addOnFailureListener(e -> {
            try {
                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                resolvableApiException.startResolutionForResult(requireActivity(), REQUEST_CHECK_CODE);
            } catch (IntentSender.SendIntentException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void getAddress() {
        Config.Log("getAddress");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(requireActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(requireActivity())
                                .removeLocationUpdates(this);
                        if (locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Config.Log(latitude + "  " + longitude);

                            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                            List<Address> addresses = null;

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            } catch (Exception exception) {
                                Config.Log(exception.getMessage());
                            }
                            if (addresses != null) {
                                getFullLocation(addresses);
                            } else {
                                getAddress();
                            }
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void getFullLocation(List<Address> addresses) {

        Address address = addresses.get(0);
        ArrayList<String> addressFragments = new ArrayList<>();
        String state = null, district = null, country = null, pin_code = null, city = null, locality = null, locality2 = null;

        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
            country = address.getCountryName();
            state = address.getAdminArea();
            district = address.getSubAdminArea();
            city = addresses.get(0).getLocality();
            pin_code = address.getPostalCode();
            locality = addresses.get(0).getSubLocality();
            locality2 = addresses.get(0).getThoroughfare();
        }
        deliverResultToReceiver(country, state, district, pin_code, city, locality, locality2);
    }

    private void deliverResultToReceiver(String country, String state, String district, String pinCode, String city, String locality, String locality2) {
        if (state == null) {
            Config.Log("TRY");
            getAddress();
            fragmentHomeBinding.alertShow.setVisibility(View.VISIBLE);
            fragmentHomeBinding.alertMsg.setText("You are not in our serviceable area");
            fragmentHomeBinding.alertMsg.setTextColor(getResources().getColor(R.color.white));
        } else {
            fragmentHomeBinding.alertShow.setVisibility(View.VISIBLE);
            fragmentHomeBinding.alertMsg.setText("You are our serviceable area");
            fragmentHomeBinding.alertMsg.setTextColor(getResources().getColor(R.color.white));

            Config.Log(state);

        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private boolean permissionCheck() {
        int permissionState = ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // permissions granted.
                    Intent login = new Intent(requireActivity(), MainActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(login);
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }   // permissions list of don't granted permission
                }
                break;
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSEnable();
//                    getAddress();
                } else {
                    Config.Log("Opsss");
                }
        }
    }

    private void inti() {
        homePageListModelList = new ArrayList<>();
        categoryGridViewModels = new ArrayList<>();
        categoryModels = new ArrayList<>();
        slideModels = new ArrayList<>();
        itemsModelList = new ArrayList<>();

        categoryModels = dbHandler.getCategoryData();
        for (int i = 0; i < categoryModels.size(); i++) {
            categoryGridViewModels.add(new CategoryGridViewModel(categoryModels.get(i).getCategoryId(), categoryModels.get(i).getCategoryImage(), categoryModels.get(i).getCategoryTitle()));
        }

        LinearLayoutManager AllViewViewLayout = new LinearLayoutManager(requireContext());
        AllViewViewLayout.setOrientation(RecyclerView.VERTICAL);
        fragmentHomeBinding.AllViewRecyclerView.setLayoutManager(AllViewViewLayout);
        homePageListModelList = new ArrayList<>();

        if (dbHandler.getSlideData().size() > 0) {
            homePageListModelList.add(new HomePageModel(0, dbHandler.getSlideData()));
        }
        if (dbHandler.getCategoryData().size() > 0) {
            homePageListModelList.add(new HomePageModel(1, "Items Gallery", categoryGridViewModels));
        }
        resentWork();

        homePageAdapter = new HomePageAdapter(homePageListModelList, activity, this);
        fragmentHomeBinding.AllViewRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();

        chalkRedirect();

    }

    private void resentWork() {
        if (dbHandler.getItemsData().size() > 0) {
            homePageListModelList.add(new HomePageModel(2, dbHandler.getItemsData(), "Resent Works"));
        }
    }

    private void chalkRedirect() {
        if (spDataSave.getBool("SELECT_IMAGE", "RE")) {
            Intent intent = new Intent(requireContext(), OtherDetailsActivity.class);
            intent.putExtra("FRAG", "IMAGE_DETAILS");
            startActivity(intent);
            spDataSave.setBool("SELECT_IMAGE", "RE", false);
        }
    }

    @Override
    public void OnClick(String CATEGORY, String TYPE, String ID) {

        if (CATEGORY.equals("CATE")) {
            if (TYPE.equals("CLICK")) {
                Intent intent = new Intent(requireContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "CATEGORY_LIST");
                startActivity(intent);
            }
        }
        if (CATEGORY.equals("CATEGORY")) {
            if (TYPE.equals("CLICK")) {
                spDataSave.setStr("IMAGE_CATEGORY", "CATEGORY", ID);
                Intent intent = new Intent(requireContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "IMAGE_GALLERY");
                startActivity(intent);
            }
        }
        if (CATEGORY.equals("ITEM")) {
            if (TYPE.equals("INQUIRY")) {
                Intent i = new Intent(activity, OtherDetailsActivity.class);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    spDataSave.setStr("MSG", "TYPE", "WORK");
                    spDataSave.setStr("MSG", "ID", ID);
                    i.putExtra("FRAG", "CONTACT");
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    SingInFragment bottomSheet = SingInFragment.newInstance(this);
                    bottomSheet.show(getFragmentManager(), "SingInFragment");
                    bottomSheet.setCancelable(false);
                }


            }
        }
        if (CATEGORY.equals("ITEM")) {
            if (TYPE.equals("SHARE")) {
                String title = dbHandler.getItemsDataWithId(ID).get(0).getTitle();
                String sub_title = dbHandler.getItemsDataWithId(ID).get(0).getDetails();
                String images = dbHandler.getItemsDataWithId(ID).get(0).getImages();
                String id = dbHandler.getItemsDataWithId(ID).get(0).getId();
                String category = dbHandler.getItemsDataWithId(ID).get(0).getCategory();

                List<SlideModel> slideModelList = new ArrayList<>();

                String link = Config.MainUrl + "Images/ItemsImage/";
                Config.Log(images);
                try {
                    JSONArray jsonArray = new JSONArray(images);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        slideModelList.add(new SlideModel(link + jsonArray.getString(i), "", ""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                progressDialogView.show();
                Uri MainLink = createDynamicLink.createDynamicLink_Advanced(
                        title, sub_title, slideModelList.get(0).getImage(), id, category);

                if (MainLink == null) {
                    Config.showLongToast("Please wait");
                } else {
                    Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLongLink(MainLink)
                            .buildShortDynamicLink()
                            .addOnCompleteListener(requireActivity(), task -> {
                                if (task.isSuccessful()) {
//                                    progressDialogView.dismiss();
                                    shortLink = task.getResult().getShortLink();
                                    shareLink(shortLink);
                                    AddShare(ID);
//                                    Config.Log(String.valueOf(shortLink));
                                } else {
//                        shortLink = "Error";
                                }
                            });

                }

            }
        }
        if (CATEGORY.equals("ITEM")) {
            if (TYPE.equals("LIKE")) {
                boolean d = new DBHandler().chalkFavItemDataWithId(ID);
                boolean b;
                if (d) {
                    b = new DBHandler().deleteFavItemDataWithId(ID);
                    if (b) {
                        AddLike(ID, "0");
                        Config.showLongToast("This Item Successfully Remove in your Favorite List");
                    }
                } else {
                    b = new DBHandler().addFavItemData(ID);
                    AddLike(ID, "1");
                    if (b) {
                        Config.showLongToast("This Item Successfully Added in your Favorite List");
                    }
                }

            }
        }
    }

    public void shareLink(Uri myDynamicLink) {
        Intent sendIntent = new Intent();
        String msg = "Hey, check this out: \n " + myDynamicLink;
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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

                Config.Log(response);
                if (requestType.equals("AddLike")) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_items = obj.getString("response_items");
                        switch (response_items) {
                            case "1":
                                dbHandler.clearItemsData();
                                JSONArray response_items_data = obj.getJSONArray("response_items_data");
                                for (int i = 0; i < response_items_data.length(); i++) {
                                    JSONObject jsonDataObject = response_items_data.getJSONObject(i);

                                    String id = jsonDataObject.getString("id");
                                    String images = jsonDataObject.getString("images");
                                    String title = jsonDataObject.getString("title");
                                    String sub_title = jsonDataObject.getString("sub_title");
                                    String category = jsonDataObject.getString("category");
                                    String status = jsonDataObject.getString("status");
                                    String like = jsonDataObject.getString("like");
                                    String share = jsonDataObject.getString("share");

                                    dbHandler.addItemsData(new ItemsModel(id, images, category, title, sub_title, like, share));
                                }
                                Config.Log("product data get complete");
                                break;
                            case "2":
                                dbHandler.clearItemsData();
                                Config.Log("product data get error");
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
                        builder.setCancelable(false);
                        builder.setMessage("Database Error!");
                        builder.setPositiveButton("Try Again", (dialog, which) -> {

                        });
                        builder.setNegativeButton("Exit", (dialog, which) -> {

                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                if (requestType.equals("AddShare")) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_items = obj.getString("response_items");
                        if (response_items.equals("1")) {
                            dbHandler.clearItemsData();
                            JSONArray response_items_data = obj.getJSONArray("response_items_data");
                            for (int i = 0; i < response_items_data.length(); i++) {
                                JSONObject jsonDataObject = response_items_data.getJSONObject(i);

                                String id = jsonDataObject.getString("id");
                                String images = jsonDataObject.getString("images");
                                String title = jsonDataObject.getString("title");
                                String sub_title = jsonDataObject.getString("sub_title");
                                String category = jsonDataObject.getString("category");
                                String status = jsonDataObject.getString("status");
                                String like = jsonDataObject.getString("like");
                                String share = jsonDataObject.getString("share");

                                dbHandler.addItemsData(new ItemsModel(id, images, category, title, sub_title, like, share));
                            }
                            Config.Log("product data get complete");
                        } else if (response_items.equals("2")) {
                            dbHandler.clearItemsData();
                            Config.Log("product data get error");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
                        builder.setCancelable(false);
                        builder.setMessage("Database Error!");
                        builder.setPositiveButton("Try Again", (dialog, which) -> {

                        });
                        builder.setNegativeButton("Exit", (dialog, which) -> {

                        });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                if ("GetAllData".equals(requestType)) {
                    Config.Log(response);
//                    progressDialogView.dismiss();
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

                        inti();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Config.Log("json decode error");
//
                    }
                }
                if ("GetAllAfterLoinData".equals(requestType)) {
                    Config.Log(response);
//                    progressDialogView.dismiss();
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
                                break;
                            }

                            case "1":
                                dbHandler.clearUserData();
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


                        inti();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Config.Log("json decode error");
                    }
                }

                fragmentHomeBinding.HomePageSwipe.setRefreshing(false);
                itemsModelList = new ArrayList<>();
                itemsModelList = dbHandler.getItemsData();
                homePageAdapter.notifyDataSetChanged();
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                builder1.setTitle("Alert Message");
                builder1.setMessage("Network error !");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Try Again",
                        (dialog, id) -> {
                            if ("GetAllData".equals(requestType)) {
                                GetAllData();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> {
                            if ("GetAllData".equals(requestType)) {
                                GetAllData();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        };
    }

    private void AddLike(String id, String c) {
        Config.Log(id);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("g", c);
        mVolleyService.CallDataVolley("AddLike", "UserData/AddLike.php", params);
    }


    private void AddShare(String id) {
        Config.Log(id);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        mVolleyService.CallDataVolley("AddShare", "UserData/AddShare.php", params);
    }

    @Override
    public void Login(boolean done) {

    }
}