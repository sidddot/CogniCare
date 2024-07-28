package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class NbackExplanationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        findViewById(R.id.start_test_button).setOnClickListener(v -> {
            startActivity(new Intent(NbackExplanationActivity.this, NBackTestActivity.class));
        });
    }
}
