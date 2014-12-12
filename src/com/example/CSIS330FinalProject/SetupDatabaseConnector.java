package com.example.CSIS330FinalProject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sean
 * on 12/12/14.
 */
public class SetupDatabaseConnector {
    private static final String DATABASE_NAME = "Setups";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    public SetupDatabaseConnector(Context context) {
//        databaseOpenHelper
    }

    public void open() {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null)
            database.close();
    }

    //stuff should happen here
    public void insertSetup() {

    }

    public void updateSetup() {

    }

    public Cursor getAllSetups() {
//        return database.query()
        return null;
    }

    public Cursor getOneSetup(long id) {
//        return database.query();
        return null;
    }

    public void deleteSetup(long id) {
        open();
//        database.delete();
        close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String createQuery = "CREATE TABLE contacts (_id integer";

//            db.execSQL(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
