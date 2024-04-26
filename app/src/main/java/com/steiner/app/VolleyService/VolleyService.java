package com.steiner.app.VolleyService;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;

import java.util.HashMap;
import java.util.Map;

public class VolleyService {

    IResult mResultCallback;
    Context mContext;

    public VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }
    public VolleyService(IResult resultCallback){
        mResultCallback = resultCallback;
        mContext = MyApplication.context;
    }

    public void CallDataVolley(final String requestType, String url, HashMap<String, String> params ){
        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MainUrl+url, response -> {
            if(mResultCallback != null)
                mResultCallback.notifySuccess(requestType, response);
        }, error -> {
            if(mResultCallback != null)
                mResultCallback.notifyError(requestType, error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    public void GetDataVolley(final String requestType, String url ){
        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MainUrl+url, response -> {
            if(mResultCallback != null)
                mResultCallback.notifySuccess(requestType, response);
        }, error -> {
            if(mResultCallback != null)
                mResultCallback.notifyError(requestType, error);
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }
}