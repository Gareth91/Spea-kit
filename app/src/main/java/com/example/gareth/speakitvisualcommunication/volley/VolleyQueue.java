package com.example.gareth.speakitvisualcommunication.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created on 15/08/2017.
 */

public class VolleyQueue {

    /**
     *
     */
    private static VolleyQueue volleyQueue;

    /**
     *
     */
    private RequestQueue requestQueue;

    /**
     *
     */
    private static String TAG = VolleyQueue.class.getName();

    /**
     *
     */
    private static Context applicationContext;

    /**
     *
     */
    private VolleyQueue(){
        requestQueue = Volley.newRequestQueue(applicationContext);
    }

    /**
     *
     * @param context
     * @return
     */
    public static synchronized VolleyQueue getInstance(Context context){

        applicationContext = context;

        if( volleyQueue == null)
            volleyQueue = new VolleyQueue();

        return volleyQueue;
    }

    /**
     *
     * @return
     */
    RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     *
     * @param req
     * @param <T>
     */
    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     *
     */
    void cancel() {
        requestQueue.cancelAll(TAG);
    }
}
