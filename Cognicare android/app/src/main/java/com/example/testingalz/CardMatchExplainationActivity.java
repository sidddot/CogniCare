package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CardMatchExplainationActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation); // Ensure this matches your XML layout filename

        findViewById(R.id.start_test_button).setOnClickListener(v -> {
            startActivity(new Intent(CardMatchExplainationActivity.this, CardMatchActivity.class));
        });
    }
}
