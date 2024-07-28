package com.example.testingalz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperSchedule extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "appointmentSchedule.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "appointments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATIENT_NAME = "patient_name";
    public static final String COLUMN_APPOINTMENT_TYPE = "appointment_type";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_REASON_FOR_VISIT = "reason_for_visit";
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PATIENT_NAME + " TEXT, " +
            COLUMN_APPOINTMENT_TYPE + " TEXT, " +
            COLUMN_CONTACT_NUMBER + " TEXT, " +
            COLUMN_REASON_FOR_VISIT + " TEXT, " +
            COLUMN_APPOINTMENT_DATE + " TEXT, " +
            COLUMN_APPOINTMENT_TIME + " TEXT);";

    public DatabaseHelperSchedule(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addAppointment(String patientName, String appointmentType, String contactNumber,
                               String reasonForVisit, String appointmentDate, String appointmentTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_NAME, patientName);
        values.put(COLUMN_APPOINTMENT_TYPE, appointmentType);
        values.put(COLUMN_CONTACT_NUMBER, contactNumber);
        values.put(COLUMN_REASON_FOR_VISIT, reasonForVisit);
        values.put(COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(COLUMN_APPOINTMENT_TIME, appointmentTime);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getAppointmentsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_APPOINTMENT_DATE + " = ?", new String[]{date});
    }

}
