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
    public List<PecsImages> getData(String categorySelected) {
        // get all data from sqlite
        List<PecsImages> imagesList = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * from "+SQLiteHelper.Table_Name+" where "+SQLiteHelper.category+" = ?",new String[]{categorySelected});
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
                imagesList.add(new PecsImages(word, images, id, category, number));
            } while (cursor.moveToNext());
        }
        return imagesList;
    }


    /**
     *
     * @param id
     * @return
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
                image = new PecsImages(word, images, id, category, number);
            } while (cursor.moveToNext());
        }
        return image;
    }


    /**
     *
     * @param word
     * @param images
     * @param category
     */
    public void insertData(String word, byte[] images, String category) {

        ContentValues values = new ContentValues();

        //Add the booking date, company name, category and customer id to booking table
        values.put(SQLiteHelper.word, word);
        values.put(SQLiteHelper.image, images);
        values.put(SQLiteHelper.category, category);
        values.put(SQLiteHelper.number, 2);

        database.insert(SQLiteHelper.Table_Name, null, values);

    }

    /**
     *
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
     * @param id
     */
    public void deleteData(int id) {

        database.delete(SQLiteHelper.Table_Name, SQLiteHelper.Column_Id+" = ?",new String[]{String.valueOf(id)});

    }



}
