package com.example.testingalz;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleAppointment extends AppCompatActivity {

    private EditText editTextPatientName;
    private Spinner spinnerAppointmentType;
    private EditText editTextContactNumber;
    private EditText editTextReasonForVisit;
    private Button buttonPickDate;
    private Button buttonPickTime;
    private Button buttonScheduleAppointment;
    private TextView textViewSelectedDate;
    private TextView textViewSelectedTime;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private DatabaseHelperSchedule dbHelper;

    public static final String CHANNEL_ID = "appointment_reminder_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);

        dbHelper = new DatabaseHelperSchedule(this);

        editTextPatientName = findViewById(R.id.editTextPatientName);
        spinnerAppointmentType = findViewById(R.id.spinnerAppointmentType);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextReasonForVisit = findViewById(R.id.editTextReasonForVisit);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        buttonScheduleAppointment = findViewById(R.id.buttonScheduleAppointment);
        textViewSelectedDate = findViewById(R.id.textViewAppointmentDate);
        textViewSelectedTime = findViewById(R.id.textViewAppointmentTime);

//        buttonViewAppointments = findViewById(R.id.buttonViewAppointments);
//        buttonViewAppointments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ScheduleAppointment.this, ViewAppointmentsActivity.class);
//                startActivity(intent);
//            }
//        });

        // Create notification channel (required for Android 8.0 and above)
        createNotificationChannel();

        // Populate Spinner with Appointment Types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appointment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAppointmentType.setAdapter(adapter);

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ScheduleAppointment.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ScheduleAppointment.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        buttonScheduleAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = editTextPatientName.getText().toString();
                String appointmentType = spinnerAppointmentType.getSelectedItem().toString();
                String contactNumber = editTextContactNumber.getText().toString();
                String reasonForVisit = editTextReasonForVisit.getText().toString();
                String appointmentDate = textViewSelectedDate.getText().toString();
                String appointmentTime = textViewSelectedTime.getText().toString();

                // Store appointment in database
                dbHelper.addAppointment(patientName, appointmentType, contactNumber, reasonForVisit, appointmentDate, appointmentTime);

                // Show a Toast as feedback
                Toast.makeText(ScheduleAppointment.this,
                        "Appointment Scheduled for " + appointmentDate + " at " + appointmentTime,
                        Toast.LENGTH_LONG).show();

                // Schedule notification
                scheduleNotification(patientName, appointmentDate, appointmentTime);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            textViewSelectedDate.setText(dateFormat.format(calendar.getTime()));
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            textViewSelectedTime.setText(timeFormat.format(calendar.getTime()));
        }
    };

    private void scheduleNotification(String patientName, String date, String time) {
        Calendar notifyTime = Calendar.getInstance();
        notifyTime.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY) ,
                calendar.get(Calendar.MINUTE)-2, 0);

        // Ensure the notification time is in the future
        if (notifyTime.before(Calendar.getInstance())) {
            notifyTime.add(Calendar.DAY_OF_MONTH, 1); // Adjust if needed
        }

        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("patientName", patientName);
        notificationIntent.putExtra("appointmentDate", date);
        notificationIntent.putExtra("appointmentTime", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Use AlarmManager to schedule the notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notifyTime.getTimeInMillis(), pendingIntent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Appointment Reminder Channel";
            String description = "Channel for appointment reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
