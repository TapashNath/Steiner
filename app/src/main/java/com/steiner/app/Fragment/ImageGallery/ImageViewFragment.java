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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
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
import com.steiner.app.R;
import com.steiner.app.databinding.FragmentImageViewBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.Content;
import com.steiner.app.utils.CreateDynamicLink;
import com.steiner.app.utils.MyApplication;
import com.steiner.app.utils.SPDataSave;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageViewFragment extends Fragment implements LoginInterface, OnUserEarnedRewardListener {
    private FragmentImageViewBinding imageViewBinding;
    private AppCompatActivity activity;


    private SPDataSave spDataSave;
    private DBHandler dbHandler;
    private List<GalleryModel> galleryModelList;
    private CreateDynamicLink createDynamicLink;
    private LoginInterface loginInterface;
    private FirebaseAuth firebaseAuth;
    private String name;
    private String id;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageViewBinding = FragmentImageViewBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        activity = ((AppCompatActivity) requireActivity());
        spDataSave = new SPDataSave();
        dbHandler = new DBHandler();
        loginInterface = this;
        firebaseAuth = FirebaseAuth.getInstance();
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        createDynamicLink = new CreateDynamicLink(activity);
        galleryModelList = new ArrayList<>();
        Config.addToolbarWithNAme(imageViewBinding.getRoot(), activity, imageViewBinding.toolbar.getId(), "IMAGE DETAILS");
        id = spDataSave.getStr("SELECT_IMAGE", "ID");
        name = dbHandler.getCategoryDataWithId(dbHandler.getGalleryDataWithId(id).get(0).getCategory_id()).get(0).getCategoryTitle();


        if (firebaseAuth.getCurrentUser() != null) {
            if (!Objects.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(), "debnathtapash56@gmail.com")) {
                AdView adView = new AdView(requireContext());
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId("ca-app-pub-3154311324989962/1919487414");
                MobileAds.initialize(requireContext(), initializationStatus -> {
                        }
                        /*LoadRewardedAd()*/);
                AdRequest adRequest = new AdRequest.Builder().build();
                imageViewBinding.adView.loadAd(adRequest);
            } else {
                imageViewBinding.adView.setVisibility(View.GONE);
            }
        } 
        galleryModelList = dbHandler.getGalleryDataWithId(id);

        String link = Config.ImageUrl;
        Glide.with(MyApplication.context)
                .load(link + galleryModelList.get(0).getImage())
                .placeholder(R.drawable.placeholder)
                .into(imageViewBinding.image);

        imageViewBinding.share.setOnClickListener(v -> {
            if (firebaseAuth.getCurrentUser() != null) {
               share();
            } else {
                SingInFragment bottomSheet = SingInFragment.newInstance(this);
                bottomSheet.show(requireActivity().getSupportFragmentManager(), "SingInFragment");
                bottomSheet.setCancelable(false);
            }


        });

        imageViewBinding.inquery.setOnClickListener(v -> {
            Intent i = new Intent(activity, OtherDetailsActivity.class);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                spDataSave.setStr("MSG", "TYPE", "IMAGE");
                spDataSave.setStr("MSG", "ID", id);
                i.putExtra("FRAG", "CONTACT");
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                SingInFragment bottomSheet = SingInFragment.newInstance(this);
                bottomSheet.show(requireFragmentManager(), "SingInFragment");
                bottomSheet.setCancelable(false);
            }

        });

        imageViewBinding.fav.setOnClickListener(v -> {
            if (dbHandler.chalkFavImgDataWithId(id)) {
                boolean d = dbHandler.deleteFavImgDataWithId(id);
                if (d) {
                    Config.showLongToast("This image successfully remove your favorite list");
                    chalkFavList(id);
                }
            } else {
                boolean d = dbHandler.addFavImgData(id);
                if (d) {
                    Config.showLongToast("This image successfully added your favorite list");
                    chalkFavList(id);
                }
            }
        });
        chalkFavList(id);
        return imageViewBinding.getRoot();
    }

    private void share() {

        String link = Config.ImageUrl;
        Uri MainLink = createDynamicLink.createDynamicLink_Advanced("BEAUTIFUL " + name + " DESIGN",  "এখন আপনার শহরে আপনার পছন্দের আসবাবপত্র।।", link + galleryModelList.get(0).getImage(), id, Content.IMAGE_DETAILS);
        if (MainLink == null) {
            Config.showLongToast("Please wait");
        } else {
            Config.Log("Start");
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(MainLink)
                    .buildShortDynamicLink()
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Config.Log("Stop1");
                            Uri shortLink = task.getResult().getShortLink();
                            shareLink(shortLink);
                        } else {
                            Config.Log("Stop2");
                        }
                    });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void chalkFavList(String id) {
        if (dbHandler.chalkFavImgDataWithId(id)) {
            imageViewBinding.fav.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_favorite_24));
            imageViewBinding.fav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ColorRed));
        } else {
            imageViewBinding.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            imageViewBinding.fav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
        }


    }


    public void shareLink(Uri myDynamicLink) {
        Intent sendIntent = new Intent();
        String msg = "Hey,\n    স্বল্প ব্যয়ে নতুন আসবাবপত্র দিয়ে আপনার ঘর সাজানোর জন্য এই অ্যাপ্লিকেশনটি এখনই ডাউনলোড করুন।  \n Download this app now to decorate your house with new furniture at low cost and join us forever. \n " + myDynamicLink;
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
        if (done){
            share();
        }
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        Config.Log("onUserEarnedReward");
    }
}