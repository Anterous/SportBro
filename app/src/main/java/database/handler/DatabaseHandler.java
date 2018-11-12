package database.handler;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import database.model.SportEvent;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(SportEvent.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SportEvent.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long inserEvent(String type, int length, int kcal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SportEvent.COLUMN_TYPE, type);
        values.put(SportEvent.COLUMN_LENGTH, length);
        values.put(SportEvent.COLUMN_KCAL, kcal);

        // insert row
        long id = db.insert(SportEvent.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public SportEvent getEvent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SportEvent.TABLE_NAME,
                new String[]{SportEvent.COLUMN_ID, SportEvent.COLUMN_TYPE,SportEvent.COLUMN_LENGTH,SportEvent.COLUMN_KCAL, SportEvent.COLUMN_TIMESTAMP},
                SportEvent.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        // prepare note object
        SportEvent sportEvent = new SportEvent(
                cursor.getInt(cursor.getColumnIndex(SportEvent.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(SportEvent.COLUMN_TYPE)),
                cursor.getInt(cursor.getColumnIndex(SportEvent.COLUMN_LENGTH)),
                cursor.getInt(cursor.getColumnIndex(SportEvent.COLUMN_KCAL)),
                cursor.getString(cursor.getColumnIndex(SportEvent.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return sportEvent;
    }

    public List<SportEvent> getAllEvents() {
        List<SportEvent> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + SportEvent.TABLE_NAME + " ORDER BY " +
                SportEvent.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SportEvent sportEvent = new SportEvent(
                        cursor.getInt(cursor.getColumnIndex(SportEvent.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SportEvent.COLUMN_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(SportEvent.COLUMN_LENGTH)),
                        cursor.getInt(cursor.getColumnIndex(SportEvent.COLUMN_KCAL)),
                        cursor.getString(cursor.getColumnIndex(SportEvent.COLUMN_TIMESTAMP)));

                notes.add(sportEvent);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getEventCount() {
        String countQuery = "SELECT  * FROM " + SportEvent.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateEvent(SportEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SportEvent.COLUMN_TYPE, event.getType());
        values.put(SportEvent.COLUMN_LENGTH, event.getLength());
        values.put(SportEvent.COLUMN_KCAL, event.getKcal());

        // updating row
        return db.update(SportEvent.TABLE_NAME, values, SportEvent.COLUMN_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void deleteEvent(SportEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SportEvent.TABLE_NAME, SportEvent.COLUMN_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }
}
