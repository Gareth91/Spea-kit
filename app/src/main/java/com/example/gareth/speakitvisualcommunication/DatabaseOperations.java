package com.example.gareth.speakitvisualcommunication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that carries out operations on the SQLite database
 * Created by Gareth on 31/07/2017.
 */

public class DatabaseOperations {

    //Create the database fields
    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;


    /**
     * Constructor which takes a Context as parameter
     * @param context
     */
    public DatabaseOperations(Context context) {

        //Instance of DatabaseWrapperClass created
        dbHelper = new SQLiteHelper(context);

    }

    /**
     * Opens the database
     * @throws SQLiteAbortException
     */
    public void open() throws SQLiteAbortException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Gets PecsImages objects which will be used in the GridView with a specific category and user
     * @return a list of PecsImages objects
     */
    public List<PecsImages> getData(String categorySelected, String user) {
        // get all data from sqlite
        List<PecsImages> imagesList = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.Table_Name+" where "+SQLiteHelper.category+" = ? AND "+SQLiteHelper.userName+" = ?",new String[]{categorySelected, user});
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(0);
                String word = cursor.getString(1);
                byte[] images = cursor.getBlob(2);
                String category = cursor.getString(3);
                int number = cursor.getInt(4);
                String userName = cursor.getString(5);
                imagesList.add(new PecsImages(word, images, id, category, userName, number));
            } while (cursor.moveToNext());
        }
        return imagesList;
    }


    /**
     * Gets PecsImages objects from the sentence table which are used within the RecyclerView.
     * @return a list of PecsImages objects
     */
    public List<PecsImages> getSentenceData() {
        // get all data from sqlite
        List<PecsImages> imagesList = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.Sentence_Table,null);
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(0);
                String word = cursor.getString(1);
                byte[] images = cursor.getBlob(2);
                int number = cursor.getInt(3);
                imagesList.add(new PecsImages(word, images, id, number));
            } while (cursor.moveToNext());
        }
        return imagesList;
    }


    /**
     * Gets a specific PecsImage object from the database
     * @param id - the id of the object
     * @return a PecsImages object
     */
    public PecsImages getItem(int id) {
        // get all data from sqlite
        PecsImages image = null;
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.Table_Name +" where "+SQLiteHelper.Column_Id+ " = ?", new String[]{String.valueOf(id)} );
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                String word = cursor.getString(1);
                byte[] images = cursor.getBlob(2);
                String category = cursor.getString(3);
                int number = cursor.getInt(4);
                String userName = cursor.getString(5);
                image = new PecsImages(word, images, id, category, userName, number);
            } while (cursor.moveToNext());
        }
        return image;
    }

    /**
     * Inserts objects into the table containing PecsImages objects for the GridView
     * @param word - The word to be entered
     * @param images - the image to be entered
     * @param category - the category of the object
     * @param userName - The users name
     */
    public void insertData(String word, byte[] images, String category, String userName) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name, category and customer id to booking table
        values.put(SQLiteHelper.word, word);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.category, category);
        values.put(SQLiteHelper.number, 2);
        values.put(SQLiteHelper.userName, userName);

        database.insert(SQLiteHelper.Table_Name, null, values);

    }

    /**
     *
     * @param username - The word to be entered
     * @param images - the image to be entered
     */
    public void insertUser(String username, byte[] images, String logName) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name, category and customer id to booking table
        values.put(SQLiteHelper.userName, username);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.logName, logName);

        database.insert(SQLiteHelper.User_Table, null, values);

    }

    /**
     *
     * @return
     */
    public List<User> getUsers(String logName) {
        // get all data from sqlite
        List<User> userList = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.User_Table+" where "+SQLiteHelper.logName+" = ?", new String[]{logName});
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                byte[] image = cursor.getBlob(1);
                userList.add(new User(name, image));
            } while (cursor.moveToNext());
        }
        return userList;
    }


    /**
     *
     * @param user
     * @return
     */
    public String getUserName(String user) {
        // get all data from sqlite
        String userName = null;
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.User_Table +" where "+SQLiteHelper.userName+ " = ?", new String[]{user});
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                 userName= cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return userName;
    }

    /**
     *
     * @param user
     * @return
     */
    public User getUser(String user, String logName) {
        // get all data from sqlite
        User person = null;
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.User_Table +" where "+SQLiteHelper.userName+ " = ? And "+SQLiteHelper.logName+" = ?", new String[]{user, logName});
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                String name = cursor.getString(0);
                byte[] image = cursor.getBlob(1);
                person = new User(name, image);
            } while (cursor.moveToNext());
        }
        return person;
    }



    /**
     * Inserts objects into the table containing PecsImages objects for the RecyclerView
     * @param word - The word to be entered
     * @param images - The image to be entered
     */
    public void insertSentenceData(String word, byte[] images) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name, category and customer id to booking table
        values.put(SQLiteHelper.word, word);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.number,2);

        database.insert(SQLiteHelper.Sentence_Table, null, values);

    }

    /**
     * Updates information of an object within the table associated with the GridView
     * @param word
     * @param images
     * @param category
     * @param id
     */
    public void updateData(String word, byte[] images, String category, int id) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name and customer id to booking table
        values.put(SQLiteHelper.word, word);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.category, category);
        values.put(SQLiteHelper.number, 2);

        database.update(SQLiteHelper.Table_Name, values, "_id = ?", new String[]{String.valueOf(id)});

    }

    /**
     * Updates information of an object within the table associated with the GridView
     * @param name
     * @param images
     */
    public void updateUser(String name, byte[] images, String logName) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name and customer id to booking table
        values.put(SQLiteHelper.userName, name);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.logName, logName);

        database.update(SQLiteHelper.User_Table, values, SQLiteHelper.userName+" = ?", new String[]{name});

    }

    /**
     * Deletes objects from the table associated with the GridView
     * @param id
     */
    public void deleteData(int id) {

        database.delete(SQLiteHelper.Table_Name, SQLiteHelper.Column_Id+" = ?",new String[]{String.valueOf(id)});

    }

    /**
     * Deletes objects from the table associated with th RecyclerView
     * @param id
     */
    public void deleteSentenceData(int id) {

        database.delete(SQLiteHelper.Sentence_Table, SQLiteHelper.Column_Id+" = ?",new String[]{String.valueOf(id)});

    }

    /**
     * Deletes objects from the table associated with the GridView
     */
    public void deleteUser(String username) {

        database.delete(SQLiteHelper.User_Table, SQLiteHelper.userName+" = ?",new String[]{username});

    }


}
