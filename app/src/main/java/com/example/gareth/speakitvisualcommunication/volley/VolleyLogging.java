package com.example.gareth.speakitvisualcommunication.volley;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created on 15/08/2017.
 */

public class VolleyLogging {

    /**
     *
     */
    private static String TAG = VolleyLogging.class.getName();

    /**
     *
     * @param request
     * @param method
     * @param url
     * @param headers
     * @param body
     */
    protected static void logRequest(StringRequest request, int method, String url, Map<String, String> headers, String body ){
        String trace = "********* REQUEST:  ";
        trace += "<" + VolleyHelp.decode(method) + ">";
        trace += "<" + url + ">";
        trace += "<" + request.getBodyContentType() + ">  ";
        trace += "<" + headers.toString() + ">  ";
        trace += "<"+ body +">  ";
        Log.d(TAG,  trace );
    }

    /**
     *
     * @param response
     */
    protected static void logResponse(String response){
        String trace = "********* RESPONSE:  <" + response + ">  ";
        largeLog(TAG, trace);
    }

    /**
     *
     * @param error
     * @param url
     */
    protected static void logErrorResponse(VolleyError error, String url){
        String trace = "********* RESPONSE:  ";
        trace += "<" + url + ">  ";
        if(error.networkResponse != null){
            trace += "serviceCall:onErrorResponse: " + error.networkResponse.statusCode;
        }
        trace += "<" + error.getMessage() + ">  ";
        Log.e(TAG,  trace );
    }

    /**
     *
     * @param tag
     * @param content
     */
    protected static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }
}
