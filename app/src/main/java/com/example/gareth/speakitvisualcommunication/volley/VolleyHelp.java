package com.example.gareth.speakitvisualcommunication.volley;

/**
 * Created on 15/08/2017.
 */

public class VolleyHelp {

    /**
     *
     */
    public interface methodDescription{
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;

    }

    /**
     *
     * @param method
     * @return
     */
    static String decode(int method) {

        switch (method) {
            case methodDescription.GET:
                return "GET";
            case methodDescription.POST:
                return "POST";
            case methodDescription.PUT:
                return "PUT";
            case methodDescription.DELETE:
                return "DELETE";
            case methodDescription.HEAD:
                return "HEAD";
            case methodDescription.OPTIONS:
                return "OPTIONS";
            case methodDescription.TRACE:
                return "TRACE";
            case methodDescription.PATCH:
                return "PATCH";
        }

        return "UNKNOWN";
    }
}
