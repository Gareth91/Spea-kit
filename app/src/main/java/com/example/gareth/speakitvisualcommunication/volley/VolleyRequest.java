package com.example.gareth.speakitvisualcommunication.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created on 15/08/2017.
 */
public class VolleyRequest {

    /**
     *
     */
    private static String TAG = VolleyRequest.class.getName();

    /**
     *
     */
    private int method;

    /**
     *
     */
    private String contentType;

    /**
     *
     */
    private String url;

    /**
     *
     */
    private Map<String, String> headers;

    /**
     *
     */
    private Map<String, String> body;

    /**
     *
     */
    private Context context;

    /**
     *
     * @param context
     * @param method
     * @param contentType
     * @param url
     * @param headers
     * @param body
     */
    public VolleyRequest(Context context, int method, String contentType, String url, Map<String, String> headers, Map<String, String> body ){

        this.method = method;
        this.contentType = contentType;
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.context = context;
    }

    /**
     * Final VolleyCallback callback
     */
    public void serviceJsonCall(final VolleyCallBack callback){

        StringRequest stringRequest = new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        VolleyLogging.logResponse(response);
                        callback.onSuccess(response);

                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLogging.logErrorResponse(error,url );
                        int statusCode = 0;
                        try{
                            statusCode = error.networkResponse.statusCode;
                        }catch(Exception ex){
                            statusCode = -99;
                        }
                        callback.onError(new ErrorResponse(statusCode, error.getMessage()));
                    }}
        ){

            /**
             *
             * @return
             * @throws AuthFailureError
             */
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject jsonBody = new JSONObject();

                try {
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        jsonBody.put(entry.getKey(), entry.getValue() );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonBody.toString();

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

            /**
             *
             * @return
             */
            @Override
            public String getBodyContentType() {
                return contentType;
            }

            /**
             *
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

        };

        stringRequest.setShouldCache(false);
        VolleyQueue.getInstance(context).add(stringRequest);
        VolleyLogging.logRequest(stringRequest, method, url, headers, body.toString() );
    }


    /**
     * Final VolleyCallback callback
     */
    public void serviceJsonCall(final VolleyCallBackReturn callback){

        StringRequest stringRequest = new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        VolleyLogging.logResponse(response);
                        callback.onSuccess(response);

                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLogging.logErrorResponse(error,url );
                        int statusCode = 0;
                        try{
                            statusCode = error.networkResponse.statusCode;
                        }catch(Exception ex){
                            statusCode = -99;
                        }
                        callback.onError(new ErrorResponse(statusCode, error.getMessage()));
                    }}
        ){

            /**
             *
             * @return
             * @throws AuthFailureError
             */
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject jsonBody = new JSONObject();

                try {
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        jsonBody.put(entry.getKey(), entry.getValue() );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonBody.toString();

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

            /**
             *
             * @return
             */
            @Override
            public String getBodyContentType() {
                return contentType;
            }

            /**
             *
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

        };

        stringRequest.setShouldCache(false);
        VolleyQueue.getInstance(context).add(stringRequest);
        VolleyLogging.logRequest(stringRequest, method, url, headers, body.toString() );
    }

}
