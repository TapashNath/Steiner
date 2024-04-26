package com.steiner.app.VolleyService;

import com.android.volley.VolleyError;

public interface IResult {
    void notifySuccess(String requestType, String response);
    void notifyError(String requestType, VolleyError error);
}