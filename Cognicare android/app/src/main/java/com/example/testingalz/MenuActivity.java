package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.nback_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, NbackExplanationActivity.class));
        });
        findViewById(R.id.random_word_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, RandomWordsExplanationActivity.class));
        });
        findViewById(R.id.corsi_block_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, CorsiBlockExplanationActivity.class));
        });
        findViewById(R.id.maze_game_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, MazeGameExplainationActivity.class));
        });
        findViewById(R.id.memory_block_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, MemoryUpdatingExplanationActivity.class));
        });
        findViewById(R.id.card_matching_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, CardMatchExplainationActivity.class));
        });
        findViewById(R.id.results_button).setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, ResultsActivity.class));
        });
    }
}
