package com.example.testingalz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "testProgress.db";
    private static final int DATABASE_VERSION = 2; // Increment the version to 2 for schema change

    // Table to store daily results
    private static final String TABLE_DAILY_RESULTS = "daily_results";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_NBACK_RESULTS = "nback_results";
    private static final String COLUMN_MEMORY_UPDATING_RESULTS = "memory_updating_results";
    private static final String COLUMN_CORSI_BLOCK_RESULTS = "corsi_block_results";
    private static final String COLUMN_CARD_GAME_RESULTS = "card_game_results";
    private static final String COLUMN_RANDOM_WORDS_RESULTS = "random_words_results";
    private static final String COLUMN_MAZE_GAME_RESULTS = "maze_game_results";
    private static final String COLUMN_TOTAL_TIME = "total_time";

    // Table to store session times
    private static final String TABLE_SESSION_TIMES = "session_times";
    private static final String COLUMN_SESSION_ID = "session_id";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";

    // Table to track daily visits
    private static final String TABLE_DAILY_VISITS = "daily_visits";
    private static final String COLUMN_VISIT_DATE = "visit_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create daily results table
        String CREATE_DAILY_RESULTS_TABLE = "CREATE TABLE " + TABLE_DAILY_RESULTS + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_NBACK_RESULTS + " TEXT, " +
                COLUMN_MEMORY_UPDATING_RESULTS + " TEXT, " +
                COLUMN_CORSI_BLOCK_RESULTS + " TEXT, " +
                COLUMN_CARD_GAME_RESULTS + " TEXT, " +
                COLUMN_RANDOM_WORDS_RESULTS + " TEXT, " +
                COLUMN_MAZE_GAME_RESULTS + " TEXT, " +
                COLUMN_TOTAL_TIME + " INTEGER)";
        db.execSQL(CREATE_DAILY_RESULTS_TABLE);

        // Create session times table
        String CREATE_SESSION_TIMES_TABLE = "CREATE TABLE " + TABLE_SESSION_TIMES + " (" +
                COLUMN_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_START_TIME + " INTEGER, " +
                COLUMN_END_TIME + " INTEGER)";
        db.execSQL(CREATE_SESSION_TIMES_TABLE);

        // Create daily visits table
        String CREATE_DAILY_VISITS_TABLE = "CREATE TABLE " + TABLE_DAILY_VISITS + " (" +
                COLUMN_VISIT_DATE + " TEXT PRIMARY KEY)";
        db.execSQL(CREATE_DAILY_VISITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_DAILY_RESULTS + " ADD COLUMN " + COLUMN_CARD_GAME_RESULTS + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_DAILY_RESULTS + " ADD COLUMN " + COLUMN_RANDOM_WORDS_RESULTS + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_DAILY_RESULTS + " ADD COLUMN " + COLUMN_MAZE_GAME_RESULTS + " TEXT");
        }
    }

    // Method to insert a new session
    public void insertSession(long startTime, long endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        db.insert(TABLE_SESSION_TIMES, null, values);
    }

    // Method to get total time spent in the app
    public int getTotalTimeSpent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_END_TIME + " - " + COLUMN_START_TIME + ") FROM " + TABLE_SESSION_TIMES, null);
        int totalTime = 0;
        if (cursor.moveToFirst()) {
            totalTime = cursor.getInt(0) / 1000 / 60; // Convert milliseconds to minutes
        }
        cursor.close();
        return totalTime;
    }

    // Method to insert a daily visit
    public void insertDailyVisit(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VISIT_DATE, date);
        db.insertWithOnConflict(TABLE_DAILY_VISITS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    // Method to get all visit dates
    public Cursor getAllVisitDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_VISIT_DATE + " FROM " + TABLE_DAILY_VISITS, null);
    }

    // Method to insert daily results
    public void insertDailyResults(String date, String nbackResults, String memoryUpdatingResults, String corsiBlockResults, String cardGameResults, String randomWordsResults, String mazeGameResults, int totalTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_NBACK_RESULTS, nbackResults);
        values.put(COLUMN_MEMORY_UPDATING_RESULTS, memoryUpdatingResults);
        values.put(COLUMN_CORSI_BLOCK_RESULTS, corsiBlockResults);
        values.put(COLUMN_CARD_GAME_RESULTS, cardGameResults);
        values.put(COLUMN_RANDOM_WORDS_RESULTS, randomWordsResults);
        values.put(COLUMN_MAZE_GAME_RESULTS, mazeGameResults);
        values.put(COLUMN_TOTAL_TIME, totalTime);
        db.insertWithOnConflict(TABLE_DAILY_RESULTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // Methods to add individual results
    public void addNbackResult(int score) {
        // Get current date to associate with this result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NBACK_RESULTS, String.valueOf(score));
        values.put(COLUMN_TOTAL_TIME, getTotalTimeSpent());

        // Update existing row if it exists, else insert new row
        int rowsAffected = db.update(TABLE_DAILY_RESULTS, values, COLUMN_DATE + " = ?", new String[]{currentDate});
        if (rowsAffected == 0) {
            // No row updated, so insert new row
            values.put(COLUMN_DATE, currentDate);
            db.insert(TABLE_DAILY_RESULTS, null, values);
        }
    }

    public void addMemoryUpdatingResult(int score) {
        // Get current date to associate with this result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMORY_UPDATING_RESULTS, String.valueOf(score));
        values.put(COLUMN_TOTAL_TIME, getTotalTimeSpent());

        // Update existing row if it exists, else insert new row
        int rowsAffected = db.update(TABLE_DAILY_RESULTS, values, COLUMN_DATE + " = ?", new String[]{currentDate});
        if (rowsAffected == 0) {
            // No row updated, so insert new row
            values.put(COLUMN_DATE, currentDate);
            db.insert(TABLE_DAILY_RESULTS, null, values);
        }
    }
    public void addCorsiBlockResult(int score) {
        // Get current date to associate with this result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CORSI_BLOCK_RESULTS, String.valueOf(score));
        values.put(COLUMN_TOTAL_TIME, getTotalTimeSpent());

        // Update existing row if it exists, else insert new row
        int rowsAffected = db.update(TABLE_DAILY_RESULTS, values, COLUMN_DATE + " = ?", new String[]{currentDate});
        if (rowsAffected == 0) {
            // No row updated, so insert new row
            values.put(COLUMN_DATE, currentDate);
            db.insert(TABLE_DAILY_RESULTS, null, values);
        }
    }

    public void addRandomWordsResult(int score) {
        // Get current date to associate with this result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RANDOM_WORDS_RESULTS, String.valueOf(score));
        values.put(COLUMN_TOTAL_TIME, getTotalTimeSpent());

        // Update existing row if it exists, else insert new row
        int rowsAffected = db.update(TABLE_DAILY_RESULTS, values, COLUMN_DATE + " = ?", new String[]{currentDate});
        if (rowsAffected == 0) {
            // No row updated, so insert new row
            values.put(COLUMN_DATE, currentDate);
            db.insert(TABLE_DAILY_RESULTS, null, values);
        }
    }

    public void addCardMatchResult(int score) {
        // Get current date to associate with this result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARD_GAME_RESULTS, String.valueOf(score));
        values.put(COLUMN_TOTAL_TIME, getTotalTimeSpent());

        // Update existing row if it exists, else insert new row
        int rowsAffected = db.update(TABLE_DAILY_RESULTS, values, COLUMN_DATE + " = ?", new String[]{currentDate});
        if (rowsAffected == 0) {
            // No row updated, so insert new row
            values.put(COLUMN_DATE, currentDate);
            db.insert(TABLE_DAILY_RESULTS, null, values);
        }
    }

    public void addMazeGameResult(int score) {
        // Get current date to associate with this result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAZE_GAME_RESULTS, String.valueOf(score));
        values.put(COLUMN_TOTAL_TIME, getTotalTimeSpent());

        // Update existing row if it exists, else insert new row
        int rowsAffected = db.update(TABLE_DAILY_RESULTS, values, COLUMN_DATE + " = ?", new String[]{currentDate});
        if (rowsAffected == 0) {
            // No row updated, so insert new row
            values.put(COLUMN_DATE, currentDate);
            db.insert(TABLE_DAILY_RESULTS, null, values);
        }
    }

    // Method to get daily results for a specific date
    public Cursor getDailyResults(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DAILY_RESULTS + " WHERE " + COLUMN_DATE + " = ?", new String[]{date});
    }
}
