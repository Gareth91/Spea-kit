package com.example.gareth.speakitvisualcommunication.volley;

/**
 * Created on 15/08/2017.
 */
public interface VolleyCallBack {

    void onSuccess(String result);
    void onError(ErrorResponse response);
}
