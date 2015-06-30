package com.example.cchuang.diabetetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cchuang on 6/23/2015.
 */
public class BloodSugarDbHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedVal.db";

    private static final String TABLE_RECORD = "mRecord";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_VALUE = "value";
   // private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
   // private static final String SQL_CREATE_ENTRIES =
    //        "CREATE TABLE " + TABLE_NAME + " (" +
    //                _ID + " INTEGER PRIMARY KEY," +
    //                TEXT_TYPE + COMMA_SEP +
    //                COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
    //                COLUMN_NAME_VALUE + TEXT_TYPE  +
    //        " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_RECORD;



    /* constructor */
    public BloodSugarDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        //Create Record table
        db.execSQL("Create table mRecord (" +
            "_id integer primary key autoincrement, date string, value integer)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertRecord(int bloodVal, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_VALUE, bloodVal);
        long newRowId;
        db.insert(TABLE_RECORD, null, cv);
       // Cursor cur= db.rawQuery("Select * from "+TABLE_NAME, null);
        //int x= cur.getCount();
        //cur.close();


       // Log.d("DBG_dataBase", Integer.toString(x));
       // db.close();
    }

   public Cursor queryRecord(){
      SQLiteDatabase db = this.getReadableDatabase();
        //String[] projection = {COLUMN_NAME_DATE, COLUMN_NAME_VALUE};
        Cursor c = db.query(TABLE_RECORD, null, null, null, null, null, COLUMN_DATE + " desc");
        return c;
    }

    public Cursor GetAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COLUMN_DATE +" " + COLUMN_VALUE +
        " from " + TABLE_RECORD, null);
        return c;
    }

    public void deleteRecordAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORD,null,null);
    }
}
