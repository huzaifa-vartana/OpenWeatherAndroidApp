package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String db_name = "citylist.db";
    public static final String tb_name = "city_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "City";


    public DatabaseHelper(@Nullable Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE  " + tb_name + " ( City PRIMARY KEY) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tb_name);
        onCreate(db);

    }

    public boolean insert(String cityname) {
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, cityname);
        long return_val = database.insert(tb_name, null, contentValues);
        if (return_val == -1) {

            return false;
        } else {

            return true;
        }


    }

    public int del_data(String name) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(tb_name, "City = ?", new String[]{name});
    }

    public Cursor getallldata() {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + tb_name, null);
        return cursor;

    }

    public Cursor result(String city) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select * from " + tb_name + " where "+ COL_2 + " = '" + city + "'";
        //String query="select * from  " + tb_name + "  where  " + COL_2 + "  LIKE '% " + city + "%'";
        Cursor cursor=database.rawQuery(query,null);
        return cursor;



    }
}
