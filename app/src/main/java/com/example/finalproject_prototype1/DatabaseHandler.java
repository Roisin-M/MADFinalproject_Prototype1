package com.example.finalproject_prototype1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "highScoreDatabase";

    // Table name
    private static final String TABLE_HIGHSCORE = "highscore";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_HIGHSCORE = "highscore";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + TABLE_HIGHSCORE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_HIGHSCORE + " INTEGER" + ")";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORE);

        // Create tables again
        onCreate(db);
    }

    // Add new high score
    public void addHighscore(HighScoreClass highscore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName());
        values.put(KEY_HIGHSCORE, highscore.getHighscore());

        // Inserting Row
        db.insert(TABLE_HIGHSCORE, null, values);
        db.close(); // Closing database connection
    }

    // Get a single high score by ID
    public HighScoreClass getHighscoreClass(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HIGHSCORE, new String[]{KEY_ID, KEY_NAME, KEY_HIGHSCORE},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        HighScoreClass highscore = new HighScoreClass(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2))
        );

        cursor.close();
        return highscore;
    }

    // Get all high scores
    public List<HighScoreClass> getAllHighscore() {
        List<HighScoreClass> highscoreList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HighScoreClass highscore = new HighScoreClass();
                highscore.setID(Integer.parseInt(cursor.getString(0)));
                highscore.setName(cursor.getString(1));
                highscore.setHighscore(Integer.parseInt(cursor.getString(2)));
                highscoreList.add(highscore);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return highscoreList;
    }

    // Get top 5 high scores
    public List<HighScoreClass> top5Highscore() {
        List<HighScoreClass> topFiveHighscoreList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIGHSCORE + " ORDER BY " + KEY_HIGHSCORE +
                " DESC LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HighScoreClass highscore = new HighScoreClass();
                highscore.setID(Integer.parseInt(cursor.getString(0)));
                highscore.setName(cursor.getString(1));
                highscore.setHighscore(Integer.parseInt(cursor.getString(2)));
                topFiveHighscoreList.add(highscore);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return topFiveHighscoreList;
    }

    // Update a high score
    public int updateHighscore(HighScoreClass highscore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName());
        values.put(KEY_HIGHSCORE, highscore.getHighscore());

        return db.update(TABLE_HIGHSCORE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(highscore.getID())});
    }

    // Delete a high score
    public void deleteHighscore(HighScoreClass highscore) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIGHSCORE, KEY_ID + " = ?",
                new String[]{String.valueOf(highscore.getID())});
        db.close();
    }

    // Get count of high scores
    public int getHighscoreCount() {
        String countQuery = "SELECT * FROM " + TABLE_HIGHSCORE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    // Empty the high scores table
    public void emptyHighscore() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HIGHSCORE);
        db.close();
    }
}
