package com.steiner.app.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.steiner.app.Fragment.Category.CategoryListFragment;
import com.steiner.app.Fragment.Contact.ContactFragment;
import com.steiner.app.Fragment.ImageGallery.ImageGalleryFragment;
import com.steiner.app.Fragment.ImageGallery.ImageViewFragment;
import com.steiner.app.Fragment.Privacy.AboutFragment;
import com.steiner.app.Fragment.Privacy.PrivacyFragment;
import com.steiner.app.R;
import com.steiner.app.utils.Content;

public class OtherDetailsActivity extends AppCompatActivity {
    private String FRAG_NAME;
    private FrameLayout frameLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_details);
        frameLayout = findViewById(R.id.ProductFrameLayout);
        if (getIntent().getExtras() == null) {
            FRAG_NAME = "CONTACT";
        }else {
            FRAG_NAME = getIntent().getExtras().getString("FRAG");
        }

        if (FRAG_NAME != null) {
            switch (FRAG_NAME) {
                case "CATEGORY_LIST":
                    setFragment(new CategoryListFragment(), Content.CATEGORY_LIST);
                    break;
                case "CONTACT":
                    setFragment(new ContactFragment(), Content.CONTACT_FRAGMENT);
                    break;
                case "IMAGE_GALLERY":
                    setFragment(new ImageGalleryFragment(), Content.IMAGE_GALLERY);
                    break;
                case "IMAGE_DETAILS":
                    setFragment(new ImageViewFragment(), Content.IMAGE_D$TAILS);
                    break;
                case "PRIVACY":
                    setFragment(new PrivacyFragment(), Content.PRIVACY);
                    break;
                case "ABOUT":
                    setFragment(new AboutFragment(), Content.ABOUT);
                    break;
                case "NO_INTERNET":
                    setFragment(new NoInternetFragment(), Content.NO_INTERNET);
                    break;
                default:
                    setFragment(new ListFragment(), -1);
            }
        }



        
    }
    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

        }
    };

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }

    }

    public void setFragment(Fragment fragment, int fragmentNO) {
        Content.CURRENT_FRAGMENT = fragmentNO;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(String.valueOf(fragmentNO));
        fragmentTransaction.commit();
    }
}

