package com.example.testingalz;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryUpdatingActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonStart;

    private List<Character> alphabetSequence;
    private char questionChar;
    private String currentAnswer;
    private int score;
    private boolean isQuestionAsked;
    private int currentIndex;
    private DatabaseHelper databaseHelper; // Added DatabaseHelper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_test);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonStart = findViewById(R.id.buttonStart);

        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuestionAsked) {
                    currentAnswer = "Yes";
                    checkAnswer();
                }
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuestionAsked) {
                    currentAnswer = "No";
                    checkAnswer();
                }
            }
        });
    }

    private void startGame() {
        alphabetSequence = generateAlphabetSequence();
        score = 0;
        showReadyMessage();
    }

    private List<Character> generateAlphabetSequence() {
        List<Character> sequence = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            char randomChar = (char) ('A' + random.nextInt(26)); // A to Z
            sequence.add(randomChar);
        }
        return sequence;
    }

    private void showReadyMessage() {
        textViewQuestion.setText("Get ready to remember the sequence!");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlphabetsSequentially();
            }
        }, 2000);
    }

    private void showAlphabetsSequentially() {
        isQuestionAsked = false;
        currentIndex = 0;
        textViewQuestion.setText("");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex < alphabetSequence.size()) {
                    char currentChar = alphabetSequence.get(currentIndex);
                    textViewQuestion.setText(String.valueOf(currentChar));

                    currentIndex++;
                    handler.postDelayed(this, 1000);
                } else {
                    askQuestion();
                }
            }
        }, 1000);
    }

    private void askQuestion() {
        isQuestionAsked = true;
        Random random = new Random();
        int randomIndex = random.nextInt(alphabetSequence.size());
        questionChar = alphabetSequence.get(randomIndex);

        textViewQuestion.setText("Is '" + questionChar + "' present in the sequence?");
    }

    private void checkAnswer() {
        boolean isPresent = alphabetSequence.contains(questionChar);
        boolean userSaidYes = "Yes".equals(currentAnswer);

        if ((userSaidYes && isPresent) || (!userSaidYes && !isPresent)) {
            score++;
            Toast.makeText(MemoryUpdatingActivity.this, "Correct! Score: " + score, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MemoryUpdatingActivity.this, "Incorrect. Score: " + score, Toast.LENGTH_SHORT).show();
        }

        // Save result to database
        databaseHelper.addMemoryUpdatingResult(score);

        showReadyMessage();
    }
}
