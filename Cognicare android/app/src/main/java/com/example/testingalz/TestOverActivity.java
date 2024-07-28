package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TestOverActivity extends AppCompatActivity {

    private TestProgressManager testProgressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_over);

        testProgressManager = TestProgressManager.getInstance();

        // Assuming you have the result to add, for example purpose:
        int sampleScore = 5; // Replace with actual score
        testProgressManager.addNbackResult(sampleScore);

        Button returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity or previous activity
                Intent intent = new Intent(TestOverActivity.this, MainActivity.class); // Adjust MainActivity.class as necessary
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
