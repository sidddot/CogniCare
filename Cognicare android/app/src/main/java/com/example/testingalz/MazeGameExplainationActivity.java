package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import  android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MazeGameExplainationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        String explanationText = getIntent().getStringExtra("EXPLANATION_TEXT");

        TextView explanationTextView = findViewById(R.id.explanation_text);
        explanationTextView.setText(explanationText);

        findViewById(R.id.start_test_button).setOnClickListener(v -> {
            startActivity(new Intent(MazeGameExplainationActivity.this, MazeGameActivity.class));
        });
    }

    public void ReturntoMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}