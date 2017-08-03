package com.example.gareth.speakitvisualcommunication;

import android.app.Application;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gareth on 02/08/2017.
 */

public class SentenceList extends Application {


    private ArrayList<PecsImages> sentenceList;
    /**
     * @return
     */
    public ArrayList<PecsImages> getSentenceList() {
        return sentenceList;
    }

    /**
     * @param sentenceList
     */
    public void setSentenceList(ArrayList<PecsImages> sentenceList) {
        this.sentenceList = sentenceList;
    }


}
