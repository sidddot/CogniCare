package com.example.testingalz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewAppointmentsActivity extends AppCompatActivity {

    private CompactCalendarView compactCalendarView;
    private TextView textViewAppointmentDetails;
    private DatabaseHelperSchedule dbHelper;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        dbHelper = new DatabaseHelperSchedule(this);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        textViewAppointmentDetails = findViewById(R.id.textViewAppointmentDetails);

        loadAppointmentsIntoCalendar();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                showAppointmentsForSelectedDate(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
    }

    private void loadAppointmentsIntoCalendar() {
        Cursor cursor = dbHelper.getAllAppointments();
        if (cursor.moveToFirst()) {
            do {
                String appointmentDate = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_APPOINTMENT_DATE));
                String appointmentTime = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_APPOINTMENT_TIME));
                String patientName = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_PATIENT_NAME));

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Date date = sdf.parse(appointmentDate + " " + appointmentTime);
                    Event event = new Event(android.R.color.holo_blue_dark, date.getTime(), patientName);
                    compactCalendarView.addEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showAppointmentsForSelectedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDate = sdf.format(date);
        Cursor cursor = dbHelper.getAppointmentsByDate(selectedDate);

        if (cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder();
            do {
                String patientName = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_PATIENT_NAME));
                String appointmentType = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_APPOINTMENT_TYPE));
                String contactNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_CONTACT_NUMBER));
                String reasonForVisit = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_REASON_FOR_VISIT));
                String appointmentTime = cursor.getString(cursor.getColumnIndex(DatabaseHelperSchedule.COLUMN_APPOINTMENT_TIME));

                sb.append("Patient Name: ").append(patientName).append("\n");
                sb.append("Appointment Type: ").append(appointmentType).append("\n");
                sb.append("Contact Number: ").append(contactNumber).append("\n");
                sb.append("Reason for Visit: ").append(reasonForVisit).append("\n");
                sb.append("Appointment Time: ").append(appointmentTime).append("\n\n");
            } while (cursor.moveToNext());

            textViewAppointmentDetails.setText(sb.toString());
        } else {
            textViewAppointmentDetails.setText("No appointments for this date.");
        }
        cursor.close();
    }
}
