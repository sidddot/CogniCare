package com.example.testingalz;
import com.example.testingalz.DatabaseHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class CorsiBlockTestActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView instructions;
    private Button startButton;
    private TextView scoreTextView;
    private Button restartButton;
    private Button continueButton;

    private ArrayList<Integer> sequence = new ArrayList<>();
    private ArrayList<Integer> userInput = new ArrayList<>();
    private int score = 0;
    private boolean showingSequence = false;
    private DatabaseHelper databaseHelper; // Added DatabaseHelper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corsi_block); // Ensure this matches your XML layout filename

        gridLayout = findViewById(R.id.gridLayout);
        instructions = findViewById(R.id.instructions);
        startButton = findViewById(R.id.startButton);
        scoreTextView = findViewById(R.id.tv_score);
        restartButton = findViewById(R.id.restartButton);
        continueButton = findViewById(R.id.continueButton);

        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        setupGrid();

        startButton.setOnClickListener(v -> startGame());
        restartButton.setOnClickListener(v -> restartGame());
        continueButton.setOnClickListener(v -> continueGame());
    }

    private void setupGrid() {
        for (int i = 0; i < 16; i++) { // 4x4 grid (16 blocks)
            Button button = new Button(this);
            button.setLayoutParams(new GridLayout.LayoutParams());
            button.setTag(i);
            gridLayout.addView(button);
            button.setOnClickListener(v -> {
                if (!showingSequence && userInput.size() < sequence.size()) {
                    int tappedIndex = (int) v.getTag();
                    userInput.add(tappedIndex);
                    flashButton(v); // Flash color
                    if (userInput.size() == sequence.size()) {
                        checkSequence();
                    }
                }
            });
        }
    }

    private void flashButton(final View v) {
        final int originalColor = ((Button) v).getCurrentTextColor();
        v.setBackgroundColor(Color.YELLOW);
        new Handler().postDelayed(() -> v.setBackgroundColor(originalColor), 500);
    }

    private void generateSequence() {
        Random random = new Random();
        sequence.clear();
        for (int i = 0; i < 5; i++) {
            sequence.add(random.nextInt(16));
        }
    }

    private void playSequence() {
        userInput.clear();
        showingSequence = true;
        new Handler().postDelayed(() -> showSequence(0), 1000);
    }

    private void showSequence(final int index) {
        if (index < sequence.size()) {
            final int blockIndex = sequence.get(index);
            final Button button = (Button) gridLayout.getChildAt(blockIndex);
            flashButton(button); // Flash color
            new Handler().postDelayed(() -> showSequence(index + 1), 1000);
        } else {
            showingSequence = false;
            enableUserInput();
            restartButton.setVisibility(View.VISIBLE);
        }
    }

    private void enableUserInput() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setEnabled(true);
        }
    }

    private void disableUserInput() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setEnabled(false);
        }
    }

    private void checkSequence() {
        disableUserInput();

        boolean sequencesMatch = true;
        for (int i = 0; i < sequence.size(); i++) {
            if (!sequence.get(i).equals(userInput.get(i))) {
                sequencesMatch = false;
                break;
            }
        }

        boolean finalSequencesMatch = sequencesMatch;
        new Handler().postDelayed(() -> {
            if (finalSequencesMatch) {
                score++;
                scoreTextView.setText("Score: " + score);
                if (score % 3 == 0) {
                    continueButton.setVisibility(View.VISIBLE);
                } else {
                    playSequence();
                }
            } else {
                gameOver();
            }
        }, 500);
    }

    private void gameOver() {
        instructions.setText("Game Over! Your score: " + score);
        restartButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
        databaseHelper.addCorsiBlockResult(score); // Add result to the database
        score = 0;
        scoreTextView.setText("Score: " + score);
        userInput.clear();
        disableUserInput();
    }

    public void startGame() {
        startButton.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
        instructions.setText("Remember the sequence and tap them in order:");
        generateSequence();
        playSequence();
    }

    public void restartGame() {
        restartButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
        instructions.setText("Remember the sequence and tap them in order:");
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setVisibility(View.VISIBLE);
        }
        generateSequence();
        playSequence();
    }

    public void continueGame() {
        continueButton.setVisibility(View.INVISIBLE);
        instructions.setText("Remember the new sequence and tap them in order:");
        generateSequence();
        playSequence();
    }

    public void ReturntoMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
