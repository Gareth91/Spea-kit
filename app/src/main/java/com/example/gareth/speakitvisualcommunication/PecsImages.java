package com.example.gareth.speakitvisualcommunication;

import java.io.Serializable;

/**
 * Created by Gareth on 27/07/2017.
 * Class is used to create objects which will be used to communicate.
 */



public class PecsImages implements Serializable {

    /**
     * The word to be spoken
     */
    private String word;

    /**
     * The image associated with the word to be spoken
     */
    private int image;

    /**
     * PECS image Byte Code
     * Anthony
     */
    private byte[] images;

    /**
     * ID for accessing Images
     * Anthony
     */
    private int id;

    /**
     * The category the iage and word are in.
     */
    private String category;

    /**
     * The users name
     */
    private String userName;

    /**
     * Number which distinguishes whether image is in drawable or from database
     */
    private int number;

    /**
     * Default Constructor that takes no arguments
     */
    public PecsImages() {

    }

    /**
     * Constructor that takes arguments to set the PecsImages
     * objects that are created from drawable folder images.
     * @param word  - The word to be spoken
     * @param image - The PECS image associated with the word to be spoken
     */
    public PecsImages(String word, int image, int number) {
        this.word = word;
        this.image = image;
        this.number = number;
    }

    /**
     * Constructor that takes arguments to set the PecsImages objects
     * that are used to add to the recyclerView to allow user to build
     * sentences.
     * @param word  - The word to be spoken
     * @param images - The PECS image associated with the word to be spoken
     */
    public PecsImages(String word, byte[] images, int id, int number) {
        this.word = word;
        this.images = images;
        this.id = id;
        this.number = number;
    }

    /**
     * Constructor that takes arguments to set the PecsImages objects
     * that are created using image from the database.
     * @param word -The word to be spoken
     * @param images - The PECS image associated with the word to be spoken
     * @param id - The id of the image
     * @param category - The category the image object is in
     * @param userName - The users name
     * @param number - Number to indicate whether it is a drawable or from database
     */
    public PecsImages(String word, byte[] images, int id, String category, String userName, int number) {
        this.word = word;
        this.images = images;
        this.id = id;
        this.category = category;
        this.userName = userName;
        this.number = number;
    }

    /**
     *
     * @param word
     * @param images
     * @param id
     * @param userName
     */
    public PecsImages(String word, byte[] images, int id, String userName) {
        this.word = word;
        this.images = images;
        this.id = id;
        this.category = category;
        this.userName = userName;
        this.number = number;
    }

    /**
     * Gets the Image
     * @return a byte array representing the image
     */
    public byte[] getImages() {
        return images;
    }

    /**
     * Sets the Image
     * @param images
     *         - The image to be uploaded to database
     */
    public void setImages(byte[] images) {
        this.images = images;
    }

    /**
     * Gets the image ID
     * @return an int representing the id of the image
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Image ID
     * @param id
     *       - The id of an image
     */
    public void setId(int id) {
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
     * @return an int for the image resource
     */
    public int getImage() {
        return image;
    }

    /**
     * Sets the image resource
     * @param image
     *        - The image associated with the word to be spoken
     */
    public void setImage(int image) {
        this.image = image;
    }

    /**
     * Gets the number which indicates whether image is from database or
     * from drawable
     * @return an int representing where the image is stored
     */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets the category the object is in
     * @return a String representing the category the object is in
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the userName
     * @param userName
     *      The users name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the username
     * @return a String representing the users name
     */
    public String getUserName() {
        return userName;
    }


}




