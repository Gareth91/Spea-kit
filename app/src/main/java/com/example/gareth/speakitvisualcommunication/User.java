package com.example.gareth.speakitvisualcommunication;

/**
 * Created by Gareth on 07/08/2017.
 */

public class User {

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private byte[] image;

    /**
     *
     * @param userName
     * @param image
     */
    public User(String userName, byte[] image) {
        this.userName = userName;
        this.image = image;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return
     */
    public  byte[] getImage() {
        return image;
    }

}
