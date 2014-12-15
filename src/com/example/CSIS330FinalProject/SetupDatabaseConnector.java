package com.example.CSIS330FinalProject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by sean
 * on 12/12/14.
 */
public class SetupDatabaseConnector {
    private static final String DATABASE_NAME = "UserSetupsThree";
    private SQLiteDatabase databasetwo;
    private DatabaseOpenHelper databaseOpenHelper;
    private static final String FIRST = "Third";

    public SetupDatabaseConnector(Context context) {

        databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 2);
    }

    public void open() {
        databasetwo = databaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (databasetwo != null)
            databasetwo.close();
    }

    //stuff should happen here
    public  void insertSetup(String name, int channels, String people, String instruments, String phones) {
        ContentValues newSetup = new ContentValues();
        newSetup.put("name", name);
        newSetup.put("channels", channels);
        newSetup.put("people", people);
        newSetup.put("instruments", instruments);
        newSetup.put("phones", phones);

        open();
        databasetwo.insert(FIRST, null, newSetup);
        close();

    }

    public void updateSetup(long id, String people, String instruments, String phones) {
        ContentValues editSetup = new ContentValues();
        editSetup.put("people", people);
        editSetup.put("instruments", instruments);

        open();
        databasetwo.update(FIRST, editSetup, "_id=" + id, null);
        close();
    }

    public Cursor getAllSetups() {
        return databasetwo.query(FIRST, new String[] {"_id", "name"}, null, null, null, null, "name");

    }

    public Cursor getOneSetup(long id) {
        return databasetwo.query(FIRST, null, "_id=" +id, null, null, null, null);
    }

    public void deleteSetup(long id) {
        open();
        databasetwo.delete(FIRST, "_id='"+id+"'", null);
        close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d("blarg", "blarg");
            String createQuery = "CREATE TABLE "+FIRST+
                    " (_id integer primary key autoincrement, name TEXT, channels integer, people TEXT, instruments TEXT, phones TEXT );";

            db.execSQL(createQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
