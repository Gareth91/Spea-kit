package com.example.gareth.speakitvisualcommunication;

/**
 * Created by Gareth on 27/07/2017.
 */

public class PecsImages {

    /**
     * The word to be spoken
     */
    private String word;

    /**
     * The PECS image associated with the word to be spoken
     */
    private int image;

    /**
     * Default Constructor that takes no arguments
     */
    public PecsImages() {

    }

    /**
     * Constructor that takes arguments
     *
     * @param word  - The word to be spoken
     * @param image - The PECS image associated with the word to be spoken
     */
    public PecsImages(String word, int image) {
        this.word = word;
        this.image = image;
    }

    /**
     * The word to be spoken
     * @return a String representing a word
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the word
     * @param word
     *      - A word which will be spoken
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * An image which represents the word to be spoken
     * @return a int for the image resource
     */
    public int getImage() {
        return image;
    }

    /**
     * Sets the image resource
     * @param image
     */
    public void setImage(int image) {
        this.image = image;
    }
}


