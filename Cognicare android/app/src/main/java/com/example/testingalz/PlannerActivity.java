package com.example.testingalz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlannerActivity extends AppCompatActivity {

    private ListView activityListView;
    private Button addActivityButton;
    private List<String> activityList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        activityListView = findViewById(R.id.activityListView);
        addActivityButton = findViewById(R.id.addActivityButton);

        // Initialize ArrayAdapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityList);
        activityListView.setAdapter(adapter);

        addActivityButton.setOnClickListener(v -> {
            if (activityList.size() < 10) {
                AddActivityDialog dialog = new AddActivityDialog();
                dialog.setListener((activity, hour, minute) -> {
                    String activityInfo = activity + " at " + hour + ":" + (minute < 10 ? "0" : "") + minute;
                    activityList.add(activityInfo);
                    adapter.notifyDataSetChanged();
                    setAlarm(activity, hour, minute);
                });
                dialog.show(getSupportFragmentManager(), "AddActivityDialog");
            } else {
                Toast.makeText(PlannerActivity.this, "Max 10 activities allowed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlarm(String activity, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);


        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.MINUTE, 2);
        }

        Intent intent = new Intent(this, ReminderReciever.class);
        intent.putExtra("activity", activity);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, activityList.size(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set for: " + hour + ":" + (minute < 10 ? "0" : "") + minute, Toast.LENGTH_SHORT).show();
    }
}
