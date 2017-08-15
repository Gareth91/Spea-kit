package com.example.gareth.speakitvisualcommunication.volley;

/**
 * Created on 15/08/2017.
 */
public class ErrorResponse {

    /**
     *
     */
    private int statuscode;

    /**
     *
     */
    private String message;

    /**
     *
     * @param statuscode
     * @param message
     */
    public ErrorResponse(int statuscode, String message) {
        this.statuscode = statuscode;
        this.message = message;
    }

    /**
     *
     * @return
     */
    public int getStatuscode() {
        return statuscode;
    }

    /**
     *
     * @param statuscode
     */
    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
