package com.example.mysynccontactapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mysynccontactapp.retrofit.res.SyncContactResBody;

public class AppDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AppDb.db";

    private static final String SQL_CREATE_FRIEND_ENTRY =
            "CREATE TABLE " + AppDbContract.FriendEntry.TABLE_NAME + " (" +
                    AppDbContract.FriendEntry._ID + " INTEGER PRIMARY KEY," +
                    AppDbContract.FriendEntry.COLUMN_NAME_PHONE + " TEXT UNIQUE)";

    private static final String SQL_DELETE_FRIEND_ENTRY =
            "DROP TABLE IF EXISTS " + AppDbContract.FriendEntry.TABLE_NAME;
    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FRIEND_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FRIEND_ENTRY);
        onCreate(db);
    }


    public void addFriends(SyncContactResBody syncContactResBody){
        SQLiteDatabase db = getWritableDatabase();

        for (String friendPhone : syncContactResBody.getFriends()) {
            ContentValues values = new ContentValues();
            values.put(AppDbContract.FriendEntry.COLUMN_NAME_PHONE, friendPhone);
            db.insert(AppDbContract.FriendEntry.TABLE_NAME, null, values);
        }
    }
}
