package com.steiner.app.utils.NetWorkChaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.R;


public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtils.getConnectivityStatus(context);
        if (status == null) {
            status = "NoInternet";
        }
        Intent i = new Intent(context, OtherDetailsActivity.class);
        i.putExtra("FRAG", "NO_INTERNET");
        switch (status) {
            case "NoInternet":
            case "NoInternetAvailable":
                context.startActivity(i);
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }

    }
}
