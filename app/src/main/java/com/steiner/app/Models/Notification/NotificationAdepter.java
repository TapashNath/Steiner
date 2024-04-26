package com.steiner.app.Models.Notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.steiner.app.Models.OnClickListeners;
import com.steiner.app.R;
import com.steiner.app.databinding.NotificationViewLayoutBinding;
import com.steiner.app.utils.MyApplication;

import java.util.List;

public class NotificationAdepter extends RecyclerView.Adapter<NotificationAdepter.ViewHolder> {

    private List<NotificationModel> notificationModelList;
    private AppCompatActivity activity;
    private OnClickListeners listeners;

    public NotificationAdepter(List<NotificationModel> notificationModelList, AppCompatActivity activity, OnClickListeners listeners) {
        this.notificationModelList = notificationModelList;
        this.activity = activity;
        this.listeners = listeners;
    }

    private NotificationViewLayoutBinding layoutNotificationBinding;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutNotificationBinding = NotificationViewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(layoutNotificationBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdepter.ViewHolder holder, int position) {
//        String link = Config.MainUrl + "Images/CategoryImage/";
        Glide.with(MyApplication.context)
                .load(notificationModelList.get(position).getImage())
                .placeholder(R.drawable.placeholder)
                .into(layoutNotificationBinding.notificationImage);
        layoutNotificationBinding.msg.setText(notificationModelList.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
