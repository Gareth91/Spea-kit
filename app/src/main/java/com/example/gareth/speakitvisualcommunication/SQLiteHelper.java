package com.example.gareth.speakitvisualcommunication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gareth on 31/07/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    /**
     * Database Version
     */
    private static final int Database_Version = 12;

    /**
     * Database name
     */
    private static final String Database_Name = "pecs.db";

    /**
     * Table Name
     */
    public static final String Table_Name= "images_table";

    /**
     * Table Name
     */
    public static final String Sentence_Table= "sentence_table";


    /**
     *
     */
    public static final String word = "word";

    /**
     *
     */
    public static final String Column_Id = "_id";

    /**
     *
     */
    public static final String image = "image";

    /**
     *
     */
    public  static final String category = "category";
    /**
     *
     */
    public static final String number = "number";

    /**
     *
     */
    public static final String userName = "user_name";

    /**
     *
     */
    public static final String logName = "log_name";

    /**
     *
     */
    public static final String User_Table = "user_table";


    /**
     *
     */
    private static final String Create_Table1 = "create table "+ Table_Name +" ("+ Column_Id +" integer primary key autoincrement, "
            +word+" text not null, "+image+" blob not null, "+category+" text not null, "+number+" integer not null, "+userName+" text not null)";


    /**
     *
     */
    private static final String Create_Table2 = "create table "+ Sentence_Table +" ("+ Column_Id +" integer primary key autoincrement, "
            +word+" text not null, "+image+" blob not null, "+number+" integer not null)";

//    /**
//     *
//     */
//    private static final String Create_Table3 = "create table "+ User_Table +" ("+ userName +" text primary key, "
//            +image+" blob not null, "+logName+" text not null)";

    /**
     *
     */
    private static final String Create_Table3 = "create table "+ User_Table +" ("+ Column_Id+" integer primary key autoincrement, "+logName +" text)";



    /**
     *
     * @param context
     */
    public SQLiteHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    /**
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table1);
        sqLiteDatabase.execSQL(Create_Table2);
        sqLiteDatabase.execSQL(Create_Table3);
    }


    /**
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w("TaskDBAdapter", "Upgrading from version " +oldVersion + " to " +newVersion + ", which will destroy all old data");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Sentence_Table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+User_Table);
        onCreate(sqLiteDatabase);
    }


}
