package com.steiner.app.Fragment.Privacy;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.steiner.app.databinding.FragmentAboutBinding;
import com.steiner.app.utils.Config;

public class AboutFragment extends Fragment {

    FragmentAboutBinding fragmentFavoriteBinding;
    private AppCompatActivity activity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavoriteBinding = FragmentAboutBinding.inflate(inflater, container, false);
        activity = ((AppCompatActivity) getActivity());
        Config.addToolbarWithNoBack(fragmentFavoriteBinding.getRoot(),activity,fragmentFavoriteBinding.MainToolBar.getId(),"About");

        fragmentFavoriteBinding.htmlView.loadUrl("http://interior.itstpssolution.com/API/about.html");
        return fragmentFavoriteBinding.getRoot();
    }


}