package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;

public class CardMatchActivity extends AppCompatActivity {
    ImageView curView = null;
    private int countPair = 0;
    private int score = 0;
    final int[] drawable = new int[]{
            R.drawable.c1, R.drawable.h1, R.drawable.ha1,
            R.drawable.i1, R.drawable.m1, R.drawable.q1
    };
    Integer[] pos = {0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5};
    int currentPos = -1;
    TextView scoreTextView;
    GridView gridView;
    ImageAdapter imageAdapter;
    DatabaseHelper databaseHelper; // Added DatabaseHelper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_game);
        scoreTextView = findViewById(R.id.score);
        gridView = findViewById(R.id.gridView);
        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper
        shuffleCards();
        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (currentPos < 0) {
                    currentPos = position;
                    curView = (ImageView) view;
                    ((ImageView) view).setImageResource(drawable[pos[position]]);
                } else {
                    if (currentPos == position) {
                        ((ImageView) view).setImageResource(R.drawable.d1);
                    } else if (pos[currentPos] != pos[position]) {
                        final ImageView tempView = (ImageView) view;
                        tempView.setImageResource(drawable[pos[position]]);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                curView.setImageResource(R.drawable.d1);
                                tempView.setImageResource(R.drawable.d1);
                            }
                        }, 500);
                        Toast.makeText(getApplicationContext(), "Not matching", Toast.LENGTH_SHORT).show();
                    } else {
                        ((ImageView) view).setImageResource(drawable[pos[position]]);
                        countPair++;
                        score += 10;
                        updateScore();
                        if (countPair == 6) {
                            Toast.makeText(getApplicationContext(), "You win", Toast.LENGTH_SHORT).show();
                            databaseHelper.addCardMatchResult(score); // Add result to the database
                            resetGame();
                        }
                    }
                    currentPos = -1;
                }
            }
        });
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private void shuffleCards() {
        Collections.shuffle(java.util.Arrays.asList(pos));
    }

    private void resetGame() {
        countPair = 0;
        score = 0; // Reset score to 0
        updateScore();
        shuffleCards();
        imageAdapter.notifyDataSetChanged();
    }

    public void ReturntoMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
