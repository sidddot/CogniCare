package com.example.testingalz;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Calendar;

public class ResultsActivity extends AppCompatActivity {

    private static final String TAG = "ResultsActivity";

    private TestProgressManager testProgressManager;
    private TextView nbackResultsTextView;
    private TextView memoryUpdatingResultsTextView;
    private TextView cardGameResultsTextView;
    private TextView randomWordResultsTextView;
    private TextView corsiBlockResultsTextView;
    private TextView mazeGameResultsTextView;
    private TextView totalTimeTextView;
    private TextView streakTextView;
    private CalendarView calendarView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Initialize the singleton instance
        testProgressManager = TestProgressManager.getInstance();

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Find TextView and CalendarView elements by their IDs
        totalTimeTextView = findViewById(R.id.total_time_text_view);
        streakTextView = findViewById(R.id.streak_text_view11);
        calendarView = findViewById(R.id.calendar_view);
        nbackResultsTextView = findViewById(R.id.nback_results_text_view);
        memoryUpdatingResultsTextView = findViewById(R.id.memory_updating_results_text_view);
        cardGameResultsTextView = findViewById(R.id.card_game_results_text_view);
        randomWordResultsTextView = findViewById(R.id.random_word_result_text_view);
        corsiBlockResultsTextView = findViewById(R.id.corsi_block_results_text_view);
        mazeGameResultsTextView = findViewById(R.id.maze_game_results_text_view);

        // Display results for the current date
        displayResults();

        // Set calendar view date change listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                displayDailyResults(year, month, dayOfMonth)
        );
    }

    private void displayResults() {
        // Get total time spent on app
        int totalMinutes = databaseHelper.getTotalTimeSpent();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        totalTimeTextView.setText(String.format("Total Time: %d hours %d minutes", hours, minutes));

        // Calculate and display streak
        int streak = calculateStreak();
        streakTextView.setText(String.format("Current Streak: %d days", streak));

        // Display today's results by default
        Calendar calendar = Calendar.getInstance();
        displayDailyResults(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void displayDailyResults(int year, int month, int dayOfMonth) {
        // Format date to match database format
        String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
        Log.d(TAG, "Displaying results for date: " + date);

        Cursor cursor = null;
        try {
            cursor = databaseHelper.getDailyResults(date);
            if (cursor != null && cursor.moveToFirst()) {
                // Fetch and display results
                String nbackResults = cursor.getString(cursor.getColumnIndexOrThrow("nback_results"));
                String memoryUpdatingResults = cursor.getString(cursor.getColumnIndexOrThrow("memory_updating_results"));
                String cardGameResults = cursor.getString(cursor.getColumnIndexOrThrow("card_game_results"));
                String randomWordResults = cursor.getString(cursor.getColumnIndexOrThrow("random_words_results"));
                String corsiBlockResults = cursor.getString(cursor.getColumnIndexOrThrow("corsi_block_results"));
                String mazeGameResults = cursor.getString(cursor.getColumnIndexOrThrow("maze_game_results"));

                nbackResultsTextView.setText(String.format("N-back Results: %s", nbackResults));
                memoryUpdatingResultsTextView.setText(String.format("Memory Updating Results: %s", memoryUpdatingResults));
                cardGameResultsTextView.setText(String.format("Card Game Results: %s", cardGameResults));
                randomWordResultsTextView.setText(String.format("Random Words Results: %s", randomWordResults));
                corsiBlockResultsTextView.setText(String.format("Corsi Block Results: %s", corsiBlockResults));
                mazeGameResultsTextView.setText(String.format("Maze Game Results: %s", mazeGameResults));
            } else {
                // Handle case when no results are available
                setNoResults();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void setNoResults() {
        nbackResultsTextView.setText("N-back Results: Not available");
        memoryUpdatingResultsTextView.setText("Memory Updating Results: Not available");
        cardGameResultsTextView.setText("Card Game Results: Not available");
        randomWordResultsTextView.setText("Random Words Results: Not available");
        corsiBlockResultsTextView.setText("Corsi Block Results: Not available");
        mazeGameResultsTextView.setText("Maze Game Results: Not available");
    }

    private int calculateStreak() {
        Cursor cursor = null;
        Set<String> visitDates = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            cursor = databaseHelper.getAllVisitDates();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    visitDates.add(cursor.getString(cursor.getColumnIndexOrThrow("visit_date")));
                }
            }

            if (visitDates.isEmpty()) {
                return 0;
            }

            ArrayList<Date> sortedDates = new ArrayList<>();
            for (String date : visitDates) {
                try {
                    sortedDates.add(dateFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(sortedDates);

            int streak = 1;
            int maxStreak = 1;

            for (int i = 1; i < sortedDates.size(); i++) {
                long diff = sortedDates.get(i).getTime() - sortedDates.get(i - 1).getTime();
                if (diff <= 24 * 60 * 60 * 1000) {
                    streak++;
                    maxStreak = Math.max(maxStreak, streak);
                } else {
                    streak = 1;
                }
            }

            return maxStreak;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
