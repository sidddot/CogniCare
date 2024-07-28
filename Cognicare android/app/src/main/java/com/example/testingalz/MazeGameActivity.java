package com.example.testingalz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MazeGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_game); // Make sure this layout file exists

        MazeView mazeView = findViewById(R.id.maze_view);
        mazeView.createMaze(); // You might want to expose this method or initialize in the view itself
    }
}
