package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ContactToScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity);

        // Optionally, you can find the button and set an OnClickListener programmatically
        Button buttonViewAppointments = findViewById(R.id.buttonViewAppointments);
        buttonViewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppointment(v);
            }
        });
    }

    public void viewAppointment(View view) {
        Intent intent = new Intent(ContactToScheduleActivity.this, ViewAppointmentsActivity.class);
        startActivity(intent);
    }
}
