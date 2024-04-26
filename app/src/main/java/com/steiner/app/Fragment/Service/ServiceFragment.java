package com.steiner.app.Fragment.Service;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.steiner.app.R;
import com.steiner.app.databinding.FragmentServiceBinding;

public class ServiceFragment extends BottomSheetDialogFragment {

    private FragmentServiceBinding fragmentServiceBinding;

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_service, null);
        dialog.setContentView(contentView);
//        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentServiceBinding = FragmentServiceBinding.inflate(inflater, container, false);


        fragmentServiceBinding.htmlView.loadUrl("http://interior.itstpssolution.com/API/service.html");

        return fragmentServiceBinding.getRoot();
    }


}