package com.example.testingalz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomWords extends AppCompatActivity {

    String[] words = {"a", "abbandonare", "zio", "zitto", "zona"};
    TextView textView1, textView2, textView3, timerBox;
    RadioButton radioButton1, radioButton2, radioButton3;

    Button submitButton, endButton;
    CountDownTimer countDownTimer;
    int correctAttempts = 0;
    List<String> wordList;
    Random random;
    RadioGroup radioGroup;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_word_test);

        // Initialize views
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioGroup = findViewById(R.id.radioGroup); // Correctly initialize RadioGroup from XML

        submitButton = findViewById(R.id.submit);
        endButton = findViewById(R.id.buttonEnd);
        timerBox = findViewById(R.id.timerBox);

        // Initialize word list and random
        wordList = new ArrayList<>();
        Collections.addAll(wordList, words);
        random = new Random();

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set initial random words
        setRandomWords();

        // Set onClickListener for submitButton
        submitButton.setOnClickListener(v -> {
            checkAnswer();
            resetGame();
        });

        // Set onClickListener for endButton
        endButton.setOnClickListener(v -> endTest());

        // Initialize and start countdown timer
        countDownTimer = new CountDownTimer(30000, 1000) { // 30 seconds
            @Override
            public void onTick(long millisUntilFinished) {
                // Update timer display
                timerBox.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Handle timer finish event
                timerBox.setText("Time's up!");
                disableOptions();
//                sendResult();
            }
        }.start();
    }


    // Method to set random words in TextViews and options in RadioButtons
    private void setRandomWords() {
        Collections.shuffle(wordList);

        // Display random words in TextViews
        textView1.setText(wordList.get(0));
        textView2.setText(wordList.get(1));
        textView3.setText(wordList.get(2));

        // Randomly select one option for correct answer
        int correctOption = random.nextInt(3);
        String correctAnswer = wordList.get(0) + ", " + wordList.get(1) + ", " + wordList.get(2);

        // Set other options with different random words
        String option1 = getRandomWords();
        String option2 = getRandomWords();
        String option3 = getRandomWords();

        if (correctOption == 0) {
            option1 = correctAnswer;
        } else if (correctOption == 1) {
            option2 = correctAnswer;
        } else {
            option3 = correctAnswer;
        }

        radioButton1.setText(option1);
        radioButton2.setText(option2);
        radioButton3.setText(option3);
    }

    // Method to get random words excluding the current correct answer
    private String getRandomWords() {
        Collections.shuffle(wordList);
        return wordList.get(0) + ", " + wordList.get(1) + ", " + wordList.get(2);
    }

    // Method to check if selected option is correct
    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedOption = findViewById(selectedId);
            String selectedText = selectedOption.getText().toString();
            String correctAnswer = textView1.getText().toString() + ", " +
                    textView2.getText().toString() + ", " +
                    textView3.getText().toString();
            if (selectedText.equals(correctAnswer)) {
                Toast.makeText(RandomWords.this, "Your Answer is Correct", Toast.LENGTH_SHORT).show();
                correctAttempts++;
            } else {
                Toast.makeText(RandomWords.this, "Incorrect Answer. Try Again!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RandomWords.this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to reset the game for the next round
    private void resetGame() {
        radioGroup.clearCheck();
        setRandomWords();
        countDownTimer.cancel();
        countDownTimer.start();
    }

    // Method to disable options and end the game
    private void disableOptions() {
        radioButton1.setEnabled(false);
        radioButton2.setEnabled(false);
        radioButton3.setEnabled(false);
        submitButton.setEnabled(false);
    }

    // Method to end the test and store the result in the database
    private void endTest() {
        // Store the correct attempts in the database
        databaseHelper.addRandomWordsResult(correctAttempts);

        // Close the activity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void ReturntoMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
