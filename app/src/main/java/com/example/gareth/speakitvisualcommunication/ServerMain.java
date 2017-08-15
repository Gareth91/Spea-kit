package com.example.gareth.speakitvisualcommunication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.example.gareth.speakitvisualcommunication.volley.ErrorResponse;
import com.example.gareth.speakitvisualcommunication.volley.VolleyCallBack;
import com.example.gareth.speakitvisualcommunication.volley.VolleyHelp;
import com.example.gareth.speakitvisualcommunication.volley.VolleyRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created on 15/08/2017.
 */
public class ServerMain {

    /**
     *
     * @param context
     */
    private void addImageWord(final Context context, String word, String category, String username, String number, Bitmap bitmapImage) {

        String BASE_URL = "http://10.0.2.2:8080/demo/test";

        String url = BASE_URL;

        HashMap<String, String> headers  = new HashMap<>();
        HashMap<String, String> body  = new HashMap<>();

        body.put("id", null);
        body.put("word", word);
        body.put("category", category);
        body.put("users_username", username);
        body.put("number", number);
        body.put("image", BitMapToString(bitmapImage));

        String contentType =  "application/json";
        VolleyRequest request =   new VolleyRequest(context, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


        //final Context context = this;

        request.serviceJsonCall(new VolleyCallBack(){
            @Override
            public void onSuccess(String result){
                System.out.print("CALLBACK SUCCESS: " + result);
                Toast.makeText(context, "Success " + result.length(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(ErrorResponse errorResponse){
                System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());
            }
        });
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
