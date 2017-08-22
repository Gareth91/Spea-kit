package com.example.gareth.speakitvisualcommunication;

/**
 * Created by Gareth on 22/08/2017.
 */

public class DataHolder {

    /**
     *
     */
    private String login;

    /**
     *
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     */
    private static final DataHolder holder = new DataHolder();

    /**
     *
     * @return
     */
    public static DataHolder getInstance() {
        return holder;
    }
}
