package com.steiner.app.utils;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.steiner.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIS";
    private ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String errorMessage = "";
            mReceiver = intent.getParcelableExtra(Config.RECEIVER);
            Location location = intent.getParcelableExtra(Config.LOCATION_DATA_EXTRA);

            if (location == null) {
                errorMessage = getString(R.string.no_location_data_provided);
                deliverResultToReceiver(Config.FAILURE_RESULT, errorMessage, null, null, null, null,null,null,null);
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1);
            } catch (Exception exception) {
                errorMessage = exception.getMessage();
            }

            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = getString(R.string.no_address_found);
                }
                deliverResultToReceiver(Config.FAILURE_RESULT, errorMessage, null, null, null, null, null,null,null);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                String state = null, district = null, country = null, pin_code = null, city = null, locality = null, locality2 = null;

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                    country = address.getCountryName();
                    state = address.getAdminArea();
                    district = address.getSubAdminArea();
                    city = addresses.get(0).getLocality();
                    pin_code = address.getPostalCode();
                    locality = addresses.get(0).getSubLocality();
                    locality2 = addresses.get(0).getThoroughfare();
                }
                deliverResultToReceiver(Config.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"), addressFragments), country, state, district, pin_code, city,locality,locality2);
            }
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, String country, String state, String district, String pinCode,String city,String locality,String locality2) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(Config.RESULT_DATA_KEY, message);
        bundle.putString(Config.RESULT_DATA_CN, country);
        bundle.putString(Config.RESULT_DATA_ST, state);
        bundle.putString(Config.RESULT_DATA_DT, district);
        bundle.putString(Config.RESULT_DATA_PIN, pinCode);
        bundle.putString(Config.RESULT_DATA_CT, city);
        bundle.putString(Config.RESULT_DATA_LLT, locality);
        bundle.putString(Config.RESULT_DATA_LLT2, locality2);
        mReceiver.send(resultCode, bundle);
        Config.Log(district);
    }
}
