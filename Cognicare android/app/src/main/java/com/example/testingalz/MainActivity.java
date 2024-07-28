package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private long sessionStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Record session start time
        sessionStartTime = System.currentTimeMillis();

        // Record daily visit
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        databaseHelper.insertDailyVisit(currentDate);

        findViewById(R.id.agreement_button).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AgreementActivity.class));
        });

        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            // Clear session data if necessary
            logout();
        });
    }

    private void logout() {
        // Optionally clear user session data here
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish MainActivity
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Record session end time and store session duration in the database
        long sessionEndTime = System.currentTimeMillis();
        databaseHelper.insertSession(sessionStartTime, sessionEndTime);
    }
}
