package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AgreementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
    }

    public void GotoGamesMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void GoToHealthCare(View view) {
        Intent intent=new Intent(this,HealthMain.class);
        startActivity(intent);
    }

    public void GoToActivityPlanner(View view){
        Intent intent=new Intent(this,PlannerActivity.class);
        startActivity(intent);
    }
}

