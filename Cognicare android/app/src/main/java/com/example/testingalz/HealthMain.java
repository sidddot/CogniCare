package com.example.testingalz;
import com.example.testingalz.R;

import android.content.Intent;
import android.media.metrics.BundleSession;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testingalz.R;

public class HealthMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity);
        //getSupportActionBar().setTitle("Doctor's List");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onScheduleButtonClick(View view)
    {
        Intent intent=new Intent(this,ScheduleAppointment.class);
        startActivity(intent);
    }
}