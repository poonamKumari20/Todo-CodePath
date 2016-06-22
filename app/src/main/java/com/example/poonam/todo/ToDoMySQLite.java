package com.example.poonam.todo;

/**
 * Created by poonam on 6/19/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToDoMySQLite extends SQLiteOpenHelper {
    public static final String MODULE = ToDoMySQLite.class.getName();

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "item_id";
    public static final String COLUMN_ITEM = "item_text";


    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_ITEMS
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ITEM + " text not null); ";

    public ToDoMySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MODULE, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(database);
    }

}
