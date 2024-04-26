package com.steiner.app.Activitys;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.steiner.app.R;

public class NoInternetFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        Button RetryBTN = view.findViewById(R.id.RetryBTN);
        RetryBTN.setOnClickListener(v -> requireActivity().finish());
        return view;
    }
}