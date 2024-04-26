package com.steiner.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.steiner.app.DataBase.DBHandler;
import com.steiner.app.Models.Notification.NotificationAdepter;
import com.steiner.app.Models.Notification.NotificationModel;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.databinding.FragmentNotificationBinding;
import com.steiner.app.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements OnClickListeners {
    private FragmentNotificationBinding notificationBinding;
    private AppCompatActivity activity;
    private DBHandler dbHandler;
    private List<NotificationModel> notificationModelList;
    private OnClickListeners listeners;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationBinding = FragmentNotificationBinding.inflate(inflater, container, false);
        activity = ((AppCompatActivity) getActivity());
        Config.addToolbarWithNoBack(notificationBinding.getRoot(), activity, notificationBinding.MainToolBar.getId(), "Notification");
        dbHandler = new DBHandler();
        notificationModelList = new ArrayList<>();
        listeners = this;

        showNotification();

        return notificationBinding.getRoot();
    }

    private void showNotification() {
        notificationModelList = dbHandler.getNotificationData();

        if (notificationModelList.size() > 0) {
            notificationBinding.alert.setVisibility(View.GONE);
        } else {
            notificationBinding.alert.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager AllViewViewLayout = new LinearLayoutManager(getContext());
        AllViewViewLayout.setOrientation(RecyclerView.VERTICAL);
        notificationBinding.AllViewRecyclerView.setLayoutManager(AllViewViewLayout);
        NotificationAdepter itemsAdapter = new NotificationAdepter(notificationModelList, activity, listeners);
        notificationBinding.AllViewRecyclerView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnClick(String CATEGORY, String TYPE, String ID) {

    }
}