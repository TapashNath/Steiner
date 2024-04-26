package com.steiner.app.utils.NetWorkChaker;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static String getConnectivityStatus(Context context){
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null){
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                status = "WifiOn";
                return status;
            }else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "MobileOn";
                return status;
            }else {
                status = "NoInternetAvailable";
                return status;
            }
        }
        return null;
    }
    public static boolean isGPSEnable(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
