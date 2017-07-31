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
 * Created by Gareth on 31/07/2017.
 */

public class DatabaseOperations {

    //Create the database fields
    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;


    /**
     * Constructor which takes a Context as parameter
     *
     * @param context
     */
    public DatabaseOperations(Context context) {

        //Instance of DatabaseWrapperClass created
        dbHelper = new SQLiteHelper(context);

    }

    /**
     * Opens the database
     *
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
     *
     * @return
     */
    public List<PecsImages> getData() {
        // get all data from sqlite
        List<PecsImages> imagesList = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.Table_Name, null);
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
     * @param
     */
    public List<Integer> queryData() {
        // get all data from sqlite
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select _id from "+SQLiteHelper.Table_Name, null);
        //if there are images present
        if(cursor.getCount() > 0) {
            //Move to the first row
            cursor.moveToFirst();
            do {
                list.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * @param word
     * @param images
     */
    public void insertData(String word, byte[] images) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name and customer id to booking table
        values.put(SQLiteHelper.word, word);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.number, 2);

        database.insert(SQLiteHelper.Table_Name, null, values);

    }

    /**
     * @param word
     * @param images
     * @param id
     */
    public void updateData(String word, byte[] images, int id) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name and customer id to booking table
        values.put(SQLiteHelper.word, word);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.number, 2);

        database.update(SQLiteHelper.Table_Name, values, "_id = ?", new String[]{String.valueOf(id)});

    }

    /**
     * @param id
     */
    public void deleteData(int id) {

        database.delete(SQLiteHelper.Table_Name, SQLiteHelper.Column_Id+" = ?",new String[]{String.valueOf(id)});

    }



}
