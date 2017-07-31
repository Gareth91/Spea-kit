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
     * PECS image Byte Code
     * Anthony
     */
    private byte[]images;

    /**
     * ID for accessing Images
     * Anthony
     */
    private int id;

    /**
     * gets the Image
     * Anthony
     * @return
     */
    public byte[] getImages() {
        return images;
    }

    /**
     * sets the Image
     * @param images
     */
    public void setImages(byte[] images) {
        this.images = images;
    }

    /**
     * gets the image ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * sets the Image ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

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
     * Created by Anthony
     * Constructor to handle the image byte code, word and ID
     * @param word
     * @param images
     * @param id
     */
    public PecsImages(String word, byte[] images, int id) {
        this.word = word;
        this.images = images;
        this.id = id;
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


