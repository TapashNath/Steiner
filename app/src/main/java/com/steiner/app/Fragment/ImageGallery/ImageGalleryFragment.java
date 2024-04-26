package com.steiner.app.Fragment.ImageGallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Fragment.login.LoginInterface;
import com.steiner.app.Fragment.login.SingInFragment;
import com.steiner.app.Models.Image.ImageListGridViewAdapter;
import com.steiner.app.Models.Image.GalleryModel;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.VolleyService.IResult;
import com.steiner.app.VolleyService.VolleyService;
import com.steiner.app.databinding.FragmentImageGellaryBinding;
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

public class ImageGalleryFragment extends Fragment implements OnClickListeners, LoginInterface {
    private FragmentImageGellaryBinding fragmentImageGellaryBinding;
    private List<GalleryModel> galleryModelList;
    private AppCompatActivity activity;
    private OnClickListeners listeners;
    private DBHandler dbHandler;
    private SPDataSave spDataSave;
    private CreateDynamicLink createDynamicLink;
    private String ID;
    private String name;
    private FirebaseAuth firebaseAuth;

    private IResult mResultCallback = null;
    private VolleyService mVolleyService;
    private AlertDialog.Builder builder1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentImageGellaryBinding = FragmentImageGellaryBinding.inflate(inflater, container, false);
        activity = ((AppCompatActivity) requireActivity());
        setHasOptionsMenu(true);
        dbHandler = new DBHandler();
        spDataSave = new SPDataSave();
        firebaseAuth = FirebaseAuth.getInstance();
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        createDynamicLink = new CreateDynamicLink(activity);
        listeners = this;
        builder1 = new AlertDialog.Builder(requireContext());
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, requireContext());

        ID = spDataSave.getStr("IMAGE_CATEGORY", "CATEGORY");
        name = dbHandler.getCategoryDataWithId(ID).get(0).getCategoryTitle();
        Config.addToolbarWithNAme(fragmentImageGellaryBinding.getRoot(), activity, fragmentImageGellaryBinding.categoryToolbar.getId(), "ALL " + name + " IMAGES");

        galleryModelList = new ArrayList<>();

        galleryModelList = dbHandler.getGalleryDataWithCategoryId(ID);

//        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,this);
//        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);


//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        Gallery2Adapter adepter = new Gallery2Adapter( dbHandler.getGalleryDataWithCategoryId(ID), listeners,activity);
//        fragmentImageGellaryBinding.imageList.setLayoutManager(layoutManager);
//        fragmentImageGellaryBinding.imageList.setAdapter(adepter);
//        adepter.notifyDataSetChanged();

        ImageListGridViewAdapter adapter = new ImageListGridViewAdapter(galleryModelList, listeners);
        fragmentImageGellaryBinding.imageList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        fragmentImageGellaryBinding.imageList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        GetImage();
        return fragmentImageGellaryBinding.getRoot();
    }

    private void GetImage() {
        galleryModelList = new ArrayList<>();
        HashMap<String, String> prams = new HashMap<>();
        prams.put("id", ID);
        mVolleyService.CallDataVolley("GetImage", "Steiner/GetImage/GetImage.php", prams);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void notifySuccess(String requestType, String response) {
                Config.Log(response);
                if ("GetImage".equals(requestType)) {
                    Config.Log(response);
//                    progressDialogView.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        String response_images = obj.getString("response_images");
                        switch (response_images) {
                            case "1":
                                dbHandler.clearSlideData();
                                JSONArray response_images_data = obj.getJSONArray("response_images_data");
                                for (int i = 0; i < response_images_data.length(); i++) {
                                    JSONObject jsonDataObject = response_images_data.getJSONObject(i);
                                    String id = jsonDataObject.getString("id");
                                    String image = jsonDataObject.getString("image");
                                    String categories_id = jsonDataObject.getString("categories_id");


                                    galleryModelList.add(new GalleryModel(id,image,categories_id));
                                }
                                Config.Log("slide data get complete");
                                break;
                            case "2":
                                dbHandler.clearSlideData();
                                Config.Log("slide data get error");
                                break;
                        }

                        ImageListGridViewAdapter adapter = new ImageListGridViewAdapter(galleryModelList, listeners);
                        fragmentImageGellaryBinding.imageList.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        fragmentImageGellaryBinding.imageList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        builder1.setTitle("Alert Message");
                        builder1.setMessage("Database error !");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Try Again",
                                (dialog, id) -> {
                                    if ("GetImage".equals(requestType)) {
                                        GetImage();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> {
                                    if ("GetImage".equals(requestType)) {
                                       requireActivity().onBackPressed();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
//
                    }
                }


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
                            if ("GetImage".equals(requestType)) {
                                GetImage();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> {
                            if ("GetImage".equals(requestType)) {
                                requireActivity().onBackPressed();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        };
    }
    @Override
    public void OnClick(String CATEGORY, String TYPE, String ID) {
        spDataSave.setStr("SELECT_IMAGE", "ID", ID);
        if (CATEGORY.equals("IMAGES")) {
            if (TYPE.equals("CLICK")) {
                Intent intent = new Intent(getContext(), OtherDetailsActivity.class);
                intent.putExtra("FRAG", "IMAGE_DETAILS");
                startActivity(intent);
            }
        }
        if (CATEGORY.equals("IMAGES")) {
            if (TYPE.equals("SHARE")) {
                if (firebaseAuth.getCurrentUser() != null) {
                    share();
                } else {
                    SingInFragment bottomSheet = SingInFragment.newInstance(this);
                    bottomSheet.show(requireActivity().getSupportFragmentManager(), "SingInFragment");
                    bottomSheet.setCancelable(false);
                }
            }
        }
    }

    public void shareLink(Uri myDynamicLink) {
        Intent sendIntent = new Intent();
        String msg = "Hey, স্বল্প ব্যয়ে নতুন আসবাব দিয়ে আপনার ঘর সাজানোর জন্য এই অ্যাপ্লিকেশনটি এখনই ডাউনলোড করুন।  \n Download this app now to decorate your house with new furniture at low cost and join us forever. \n " + myDynamicLink;
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Login(boolean done) {
        if (done) {
            share();
        }
    }

    private void share() {
        List<GalleryModel> galleryModels = new ArrayList<>();
        galleryModels = dbHandler.getGalleryDataWithId(ID);
        String link = Config.ImageUrl + dbHandler.getGalleryDataWithId(spDataSave.getStr("SELECT_IMAGE", "ID")).get(0).getImage();
        Uri MainLink = createDynamicLink.createDynamicLink_Advanced(
                "BEAUTIFUL " + name + " DESIGN", "এখন আপনার শহরে আপনার পছন্দের আসবাবপত্র।", link , spDataSave.getStr("SELECT_IMAGE", "ID"), Content.IMAGE_DETAILS);

        if (MainLink == null) {
            Config.showLongToast("Please wait");
        } else {
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(MainLink)
                    .buildShortDynamicLink()
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            shareLink(shortLink);
                        } else {

                        }
                    });
        }
    }
}