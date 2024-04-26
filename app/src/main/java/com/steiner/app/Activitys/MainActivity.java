package com.steiner.app.Activitys;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Fragment.FavoriteFragment;
import com.steiner.app.Fragment.Home.HomeFragment;
import com.steiner.app.Fragment.NotificationFragment;
import com.steiner.app.Fragment.ProfileFragment;
import com.steiner.app.Fragment.login.LoginInterface;
import com.steiner.app.Fragment.login.SingInFragment;
import com.steiner.app.R;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.Content;
import com.steiner.app.utils.NetWorkChaker.InternetReceiver;
import com.steiner.app.utils.SPDataSave;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoginInterface {

    private BottomNavigationView bottomNavigationView;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    private FrameLayout MainFrameLayout;
    private FloatingActionButton msg;
    private DBHandler dbHandler;
    private FirebaseAuth firebaseAuth;
    private SPDataSave spDataSave;
    private LoginInterface loginInterface;
    private BroadcastReceiver InterNetReceiver;
    private RewardedAd mRewardedAd;

    private AppUpdateManager appUpdateManager;
    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFrameLayout = findViewById(R.id.MainFrameLayout);
        dbHandler = new DBHandler();
        spDataSave = new SPDataSave();
        loginInterface = this;
        firebaseAuth = FirebaseAuth.getInstance();

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();

        if (savedInstanceState == null) {
            setFragment(new HomeFragment(), Content.HOME_FRAGMENT);
        }

        bottomNavigationView = findViewById(R.id.menu);
        msg = findViewById(R.id.msg);
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);

        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        if (dbHandler.getNotificationData().size() > 0) {
            View badge = LayoutInflater.from(this).inflate(R.layout.bottom_menu_count_layout, bottomNavigationMenuView, false);
            TextView tv = badge.findViewById(R.id.notification_badge);
            tv.setText(String.valueOf(dbHandler.getNotificationData().size()));
            itemView.addView(badge);
        }


        if (firebaseAuth.getCurrentUser() != null) {
            if (!Objects.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(), "debnathtapash56@gmail.com")) {
                LoadRewardedAd();
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(task -> {
            String msg = getString(R.string.subsribed);
            if (!task.isSuccessful()) {
                msg = getString(R.string.not_subsribed);
            }
        });
        msg.setOnClickListener(v1 -> {

            Intent i = new Intent(getApplicationContext(), OtherDetailsActivity.class);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                spDataSave.setStr("MSG", "TYPE", "NORMAL");
                i.putExtra("FRAG", "CONTACT");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                SingInFragment bottomSheet = SingInFragment.newInstance(this);
                bottomSheet.show(getSupportFragmentManager(), "SingInFragment");
                bottomSheet.setCancelable(false);
            }

        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ProfileBtn:
                    if (Content.CURRENT_FRAGMENT != Content.PROFILE_FRAGMENT) {
                        setFragment(new ProfileFragment(), Content.PROFILE_FRAGMENT);
                        msg.setVisibility(View.GONE);
                    }
                    break;
                case R.id.HomeBtn:
                    if (Content.CURRENT_FRAGMENT != Content.HOME_FRAGMENT) {
                        setFragment(new HomeFragment(), Content.HOME_FRAGMENT);
                        msg.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.FavBtn:
                    if (Content.CURRENT_FRAGMENT != Content.FAVORITE_FRAGMENT) {
                        setFragment(new FavoriteFragment(), Content.FAVORITE_FRAGMENT);
                        msg.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.NotificationBtn:
                    if (Content.CURRENT_FRAGMENT != Content.NOTIFICATION_FRAGMENT) {
                        setFragment(new NotificationFragment(), Content.NOTIFICATION_FRAGMENT);
                        msg.setVisibility(View.VISIBLE);
                    }
                    break;

            }
            return true;
        });
        InterNetReceiver = new InternetReceiver();
        brodCastReceiver();
    }


    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, FLEXIBLE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Install", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void LoadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3154311324989962/8853297512",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                        LoadRewardedAd();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Config.Log("Ad was loaded.");
                        ShowRewardedAd();
                    }
                });
    }

    private void ShowRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd.show(this, rewardItem -> {
                // Handle the reward.
                int rewardAmount = rewardItem.getAmount();
                Config.showLongToast(String.valueOf(rewardAmount));

            });
        } else {
            Config.Log("The rewarded ad wasn't ready yet.");
        }
    }


    private void brodCastReceiver() {
        registerReceiver(InterNetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(InterNetReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        brodCastReceiver();
    }


    @Override
    protected void onResume() {
        super.onResume();
        brodCastReceiver();
    }

    private void setFragment(Fragment fragment, int fragmentNO) {
        Content.CURRENT_FRAGMENT = fragmentNO;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(String.valueOf(fragmentNO));
        fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_to_right, R.anim.enter_left_to_right, R.anim.exit_to_left);
        fragmentTransaction.replace(MainFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            finish();
        } else {
            Config.showLongToast("Press once again to exit!");
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void Login(boolean done) {
    }


}