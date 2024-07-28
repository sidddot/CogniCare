package com.example.testingalz;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NBackTestActivity extends AppCompatActivity {

    private boolean isTestOver = false;
    private List<Integer> currentData = new ArrayList<>();
    private Integer question;
    private Integer answer;
    private int score = 0;

    private TestProgressManager testProgressManager;
    private DatabaseHelper databaseHelper;

    private TextView currentDataTextView;
    private TextView questionTextView;
    private View answersLayout;
    private View coverView;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nback_test);

        currentDataTextView = findViewById(R.id.current_data);
        questionTextView = findViewById(R.id.question);
        answersLayout = findViewById(R.id.answers_layout);
        coverView = findViewById(R.id.cover_view);

        testProgressManager = TestProgressManager.getInstance();
        databaseHelper = new DatabaseHelper(this);

        addNewNumber();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Prevent memory leaks
    }

    private void addNewNumber() {
        handler.postDelayed(() -> {
            currentData.add(randIncl(9));
            updateCurrentDataTextView();
            coverView.setVisibility(View.GONE);

            handler.postDelayed(() -> {
                coverView.setVisibility(View.VISIBLE);

                if (currentData.size() <= 6) {
                    addNewNumber();
                } else {
                    generateNewQuestion();
                }
            }, 2000); // Show number for 2 seconds
        }, 1200);
    }

    private void generateNewQuestion() {
        handler.postDelayed(() -> {
            if (question == null) {
                question = 1 + randIncl(6);
                updateQuestionTextView();
            }
        }, 1500);
    }

    private void submitAnswer(int value) {
        answer = value;
        checkAnswer();
    }

    private void checkAnswer() {
        if (answer == null || question == null) return;

        if (currentData.size() > question) {
            int questionedNumber = currentData.get(currentData.size() - question - 1);
            if (answer.equals(questionedNumber)) {
                score++;
                answer = null;
                question = null;
                addNewNumber();
            } else {
                isTestOver = true;
                endTest();
            }
        } else {
            isTestOver = true;
            endTest();
        }

        if (score == 5) {
            isTestOver = true;
            endTest();
        }
    }

    private void endTest() {
        testProgressManager.addNbackResult(score);
        databaseHelper.addNbackResult(score); // Save score to database
        Intent intent = new Intent(NBackTestActivity.this, TestOverActivity.class);
        startActivity(intent);
        finish(); // Ensure the current activity is finished
    }

    private void updateCurrentDataTextView() {
        StringBuilder data = new StringBuilder();
        for (int num : currentData) {
            data.append(num).append(" ");
        }
        currentDataTextView.setText(data.toString().trim());
    }

    private void updateQuestionTextView() {
        String ordinalStr = getOrdinalString(question);
        questionTextView.setText(String.format("What is the %s last digit?", ordinalStr));
        answersLayout.setVisibility(View.VISIBLE);
    }

    private String getOrdinalString(int number) {
        switch (number) {
            case 1: return "second";
            case 2: return "third";
            case 3: return "fourth";
            case 4: return "fifth";
            case 5: return "sixth";
            case 6: return "seventh";
            default: return "";
        }
    }

    private int randIncl(int max) {
        return random.nextInt(max + 1);
    }

    public void submitAnswer1(View view) {
        submitAnswer(1);
    }

    public void submitAnswer2(View view) {
        submitAnswer(2);
    }

    public void submitAnswer3(View view) {
        submitAnswer(3);
    }

    public void submitAnswer4(View view) {
        submitAnswer(4);
    }

    public void submitAnswer5(View view) {
        submitAnswer(5);
    }

    public void submitAnswer6(View view) {
        submitAnswer(6);
    }

    public void submitAnswer7(View view) {
        submitAnswer(7);
    }

    public void submitAnswer8(View view) {
        submitAnswer(8);
    }

    public void submitAnswer9(View view) {
        submitAnswer(9);
    }

    public void submitAnswer0(View view) {
        submitAnswer(0);
    }

    public void ReturntoMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
         finish();
    }
}
