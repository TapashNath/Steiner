    package com.steiner.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.Activitys.SplashActivity;
import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Fragment.login.LoginInterface;
import com.steiner.app.Fragment.login.SingInFragment;
import com.steiner.app.R;
import com.steiner.app.databinding.FragmentProfileBinding;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;
import com.steiner.app.utils.SPDataSave;

public class ProfileFragment extends Fragment implements LoginInterface {

    private FragmentProfileBinding profileBinding;
    private SPDataSave spDataSave;
    private AppCompatActivity activity;
    private DBHandler dbHandler;
    private FirebaseAuth firebaseAuth;
    private LoginInterface loginInterface;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        spDataSave = new SPDataSave();
        activity = ((AppCompatActivity) getActivity());
        dbHandler = new DBHandler();
        firebaseAuth = FirebaseAuth.getInstance();
        loginInterface = this;
        Config.addToolbarWithNoBack(profileBinding.getRoot(), activity, profileBinding.MainToolBar.getId(), "");


        profileBinding.LogIn.setOnClickListener(v -> {
            SingInFragment bottomSheet = SingInFragment.newInstance(this);
            bottomSheet.show(getFragmentManager(), "SingInFragment");
            bottomSheet.setCancelable(false);
        });
        profileBinding.policy.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), OtherDetailsActivity.class);
            i.putExtra("FRAG", "PRIVACY");
            startActivity(i);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        profileBinding.about.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), OtherDetailsActivity.class);
            i.putExtra("FRAG", "ABOUT");
            startActivity(i);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        profileBinding.logout.setOnClickListener(v -> {
            LogoutAlert();
        });

        if (spDataSave.getBool("APP_DATA", "NOTIFICATION")) {
            profileBinding.switch1.setChecked(true);
        }
        profileBinding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spDataSave.setBool("APP_DATA", "NOTIFICATION", true);
                    FirebaseMessaging.getInstance().subscribeToTopic("all_users").addOnCompleteListener(task -> {
                        String msg = getString(R.string.subsribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.not_subsribed);
                        }
                    });
                    Config.showLongToast("Notification On");
                } else {
                    spDataSave.setBool("APP_DATA", "NOTIFICATION", true);
                    FirebaseMessaging.getInstance().subscribeToTopic("users").addOnCompleteListener(task -> {
                        String msg = getString(R.string.subsribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.not_subsribed);
                        }
                    });
                    Config.showLongToast("Notification Off");
                }
            }
        });
        setProfile();
        return profileBinding.getRoot();
    }

    private void setProfile() {
        if (firebaseAuth.getCurrentUser() != null) {
            Glide.with(MyApplication.context)
                    .load(dbHandler.getUserData().get(0).getImage())
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(profileBinding.profilePic);
            profileBinding.ProfileName.setText(dbHandler.getUserData().get(0).getUsername());
            profileBinding.ProfileEmail.setText(dbHandler.getUserData().get(0).getEmail());
            profileBinding.Details.setVisibility(View.VISIBLE);
            profileBinding.LogIn.setVisibility(View.GONE);
        } else {
            profileBinding.Details.setVisibility(View.GONE);
            profileBinding.LogIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private void signOut() {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                task -> {
                    Log.d("APP_ACTIVITY", "sing out");
                    FirebaseAuth.getInstance().signOut();
                    boolean l = dbHandler.clearAll();
                    if (l) {
                        dbHandler.clearAll();
                        startActivity(new Intent(getActivity(), SplashActivity.class));
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        getActivity().finish();
                    }
                });
    }


    private void LogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("Do you want to Logout Now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel getContext() dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void Login(boolean done) {
        if (done) {
            setProfile();
        }
    }
}