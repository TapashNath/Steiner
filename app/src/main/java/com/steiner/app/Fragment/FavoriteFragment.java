package com.steiner.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Fragment.login.LoginInterface;
import com.steiner.app.Fragment.login.SingInFragment;
import com.steiner.app.Models.Image.GalleryModel;
import com.steiner.app.Models.Image.ImageListGridViewAdapter;
import com.steiner.app.Models.Items.ItemsAdapter;
import com.steiner.app.Models.Items.ItemsModel;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.R;
import com.steiner.app.Slider.SlideModel;
import com.steiner.app.VolleyService.IResult;
import com.steiner.app.VolleyService.VolleyService;
import com.steiner.app.databinding.FragmentFavoriteBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.Content;
import com.steiner.app.utils.CreateDynamicLink;
import com.steiner.app.utils.SPDataSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteFragment extends Fragment implements OnClickListeners, LoginInterface {

    FragmentFavoriteBinding fragmentFavoriteBinding;
    private DBHandler dbHandler;
    private ItemsAdapter itemsAdapter;
    private List<GalleryModel> galleryModelList;
    private List<ItemsModel> itemsModelList;
    private List<String> items;
    private List<String> images;
    private AppCompatActivity activity;
    private OnClickListeners listeners;
    private SPDataSave spDataSave;
    private CreateDynamicLink createDynamicLink;
    private FirebaseAuth firebaseAuth;
    private LoginInterface loginInterfac;

    private IResult mResultCallback = null;
    private VolleyService mVolleyService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false);
        dbHandler = new DBHandler();
        activity = ((AppCompatActivity) getActivity());
        spDataSave = new SPDataSave();
        firebaseAuth = FirebaseAuth.getInstance();
        loginInterfac = this;
        createDynamicLink = new CreateDynamicLink(activity);
        listeners = this;

        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        Config.addToolbarWithNoBack(fragmentFavoriteBinding.getRoot(),activity,fragmentFavoriteBinding.MainToolBar.getId(),"Your Favorite Items");
        setFavImage();

        fragmentFavoriteBinding.work.setOnClickListener(v -> {
            fragmentFavoriteBinding.work.setTextColor(getResources().getColor(R.color.appColor));
            fragmentFavoriteBinding.work.setBackgroundColor(getResources().getColor(R.color.white));
            fragmentFavoriteBinding.ItemView.setVisibility(View.VISIBLE);

            fragmentFavoriteBinding.item.setTextColor(getResources().getColor(R.color.white));
            fragmentFavoriteBinding.item.setBackgroundColor(getResources().getColor(R.color.appColor));
            fragmentFavoriteBinding.ImageView.setVisibility(View.GONE);
            setFavItems();
        });

        fragmentFavoriteBinding.item.setOnClickListener(v -> {
            fragmentFavoriteBinding.item.setTextColor(getResources().getColor(R.color.appColor));
            fragmentFavoriteBinding.item.setBackgroundColor(getResources().getColor(R.color.white));
            fragmentFavoriteBinding.ImageView.setVisibility(View.VISIBLE);

            fragmentFavoriteBinding.work.setTextColor(getResources().getColor(R.color.white));
            fragmentFavoriteBinding.work.setBackgroundColor(getResources().getColor(R.color.appColor));
            fragmentFavoriteBinding.ItemView.setVisibility(View.GONE);
            setFavImage();
        });


        fragmentFavoriteBinding.item.setTextColor(getResources().getColor(R.color.appColor));
        fragmentFavoriteBinding.item.setBackgroundColor(getResources().getColor(R.color.white));
        fragmentFavoriteBinding.ImageView.setVisibility(View.VISIBLE);

        fragmentFavoriteBinding.work.setTextColor(getResources().getColor(R.color.white));
        fragmentFavoriteBinding.work.setBackgroundColor(getResources().getColor(R.color.appColor));
        fragmentFavoriteBinding.ItemView.setVisibility(View.GONE);


        if (galleryModelList.size() >0 ){
            fragmentFavoriteBinding.alert.setVisibility(View.GONE);
            fragmentFavoriteBinding.ImageView.setVisibility(View.VISIBLE);
        }else {
            fragmentFavoriteBinding.alert.setVisibility(View.VISIBLE);
            fragmentFavoriteBinding.ImageView.setVisibility(View.GONE);
        }

        return fragmentFavoriteBinding.getRoot();
    }

    private void setFavItems() {
        itemsModelList = new ArrayList<>();
        items = new ArrayList<>();
        items = dbHandler.getFavItemData();

        for (int i = 0; i < items.size(); i++) {
            if (dbHandler.chalkItemsDataWithId(items.get(i))) {
                itemsModelList.addAll(dbHandler.getItemsDataWithId(items.get(i)));
            } else {
                dbHandler.clearItemsDataWithId(items.get(i));
            }
        }


        if (itemsModelList.size() >0 ){
            fragmentFavoriteBinding.alert.setVisibility(View.GONE);
            fragmentFavoriteBinding.ItemView.setVisibility(View.VISIBLE);
        }else {
            fragmentFavoriteBinding.alert.setVisibility(View.VISIBLE);
            fragmentFavoriteBinding.ItemView.setVisibility(View.GONE);
        }
        LinearLayoutManager AllViewViewLayout = new LinearLayoutManager(getContext());
        AllViewViewLayout.setOrientation(RecyclerView.VERTICAL);
        fragmentFavoriteBinding.ItemView.setLayoutManager(AllViewViewLayout);
        itemsAdapter = new ItemsAdapter(itemsModelList, getContext(), activity, listeners);
        fragmentFavoriteBinding.ItemView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
    }

    private void setFavImage() {
        galleryModelList = new ArrayList<>();
        images = new ArrayList<>();
        images = dbHandler.getFavImgData();

        for (int i = 0; i < images.size(); i++) {
            if (dbHandler.chalkGalleryDataWithId(images.get(i))) {
                galleryModelList.addAll(dbHandler.getGalleryDataWithId(images.get(i)));
            } else {
                dbHandler.clearGalleryDataWithId(images.get(i));
            }
        }
        if (galleryModelList.size() >0 ){
            fragmentFavoriteBinding.alert.setVisibility(View.GONE);
            fragmentFavoriteBinding.ImageView.setVisibility(View.VISIBLE);
        }else {
            fragmentFavoriteBinding.alert.setVisibility(View.VISIBLE);
            fragmentFavoriteBinding.ImageView.setVisibility(View.GONE);
        }
            fragmentFavoriteBinding.ImageView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ImageListGridViewAdapter adapter = new ImageListGridViewAdapter(galleryModelList, listeners);
        fragmentFavoriteBinding.ImageView.setAdapter(adapter);
    }

    @Override
    public void OnClick(String CATEGORY, String TYPE, String ID) {
        if (CATEGORY.equals("IMAGES")) {
            if (TYPE.equals("CLICK")) {
                Intent intent = new Intent(getContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "IMAGE_DETAILS");
                spDataSave.setStr("SELECT_IMAGE", "ID", ID);
                startActivity(intent);
            }
        }

      if (CATEGORY.equals("IMAGES")) {
              if (TYPE.equals("SHARE")) {

                  List<GalleryModel> galleryModels = new ArrayList<>();

                  galleryModels = dbHandler.getGalleryDataWithId(ID);
                  String link = Config.MainUrl + "Images/GalleryImage/";
                  Uri MainLink = createDynamicLink.createDynamicLink_Advanced(
                          "The Bountiful " + galleryModels.get(0).getCategory_id() + " Design", "Click and see all the details in our app", link + galleryModels.get(0).getImage(), ID, Content.IMAGE_DETAILS);

                  if (MainLink == null) {
                      Config.showLongToast("Please wait");
                  } else {
                      Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                              .setLongLink(MainLink)
                              .buildShortDynamicLink()
                              .addOnCompleteListener(getActivity(), task -> {
                                  if (task.isSuccessful()) {
//                                    progressDialogView.dismiss();
                                      Uri shortLink = task.getResult().getShortLink();
                                      shareLink(shortLink);
//                                    Config.Log(String.valueOf(shortLink));
                                  }
                              });

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
                            .addOnCompleteListener(getActivity(), task -> {
                                if (task.isSuccessful()) {
//                                    progressDialogView.dismiss();
                                    Uri shortLink = task.getResult().getShortLink();
                                    shareLink(shortLink);
//                                    Config.Log(String.valueOf(shortLink));
                                }
                            });

                }

            }
        }

        if (CATEGORY.equals("ITEM")) {
            if (TYPE.equals("INQUIRY")) {
                Intent i = new Intent(activity, OtherDetailsActivity.class);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    spDataSave.setStr("MSG","TYPE","WORK");
                    spDataSave.setStr("MSG","ID",ID);
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

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
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

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
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

            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                int errorCode = 0;
                if (error instanceof TimeoutError) {
                    errorCode = -7;
                } else if (error instanceof NoConnectionError) {
                    errorCode = -1;
                } else if (error instanceof AuthFailureError) {
                    errorCode = -6;
                } else if (error instanceof ServerError) {
                    errorCode = 0;
                } else if (error instanceof NetworkError) {
                    errorCode = -1;
                } else if (error instanceof ParseError) {
                    errorCode = -8;
                }
                Config.Log(String.valueOf(errorCode));
                ///////Call Admin Data Start
                /////////Call Admin Data End
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