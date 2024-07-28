package com.example.testingalz;
import com.example.testingalz.DatabaseHelper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeView extends View {
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Cell[][] cells;
    private Cell player, exit;
    private static final int COLS = 7, ROWS = 10;
    private static final float WALL_THICKNESS = 4;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, exitPaint, textPaint;
    private Random random;
    private int score = 0;
    private int gamesPlayed = 0;
    private DatabaseHelper databaseHelper; // Added reference to DatabaseHelper

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        databaseHelper = new DatabaseHelper(context); // Initialize DatabaseHelper
        init();
    }

    private void init() {
        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);

        exitPaint = new Paint();
        exitPaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);

        random = new Random();
        createMaze();
    }

    private Cell getNeighbour(Cell cell) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        if (cell.col > 0 && !cells[cell.col - 1][cell.row].visited)
            neighbours.add(cells[cell.col - 1][cell.row]);

        if (cell.col < COLS - 1 && !cells[cell.col + 1][cell.row].visited)
            neighbours.add(cells[cell.col + 1][cell.row]);

        if (cell.row > 0 && !cells[cell.col][cell.row - 1].visited)
            neighbours.add(cells[cell.col][cell.row - 1]);

        if (cell.row < ROWS - 1 && !cells[cell.col][cell.row + 1].visited)
            neighbours.add(cells[cell.col][cell.row + 1]);

        if (!neighbours.isEmpty()) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        }
        return null;
    }

    private void removeWall(Cell current, Cell next) {
        if (current.col == next.col && current.row == next.row + 1) {
            current.topWall = false;
            next.bottomWall = false;
        } else if (current.col == next.col && current.row == next.row - 1) {
            current.bottomWall = false;
            next.topWall = false;
        } else if (current.col == next.col + 1 && current.row == next.row) {
            current.leftWall = false;
            next.rightWall = false;
        } else if (current.col == next.col - 1 && current.row == next.row) {
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    public void createMaze() {
        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        cells = new Cell[COLS][ROWS];

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }

        player = cells[0][0];
        exit = cells[COLS - 1][ROWS - 1];

        current = cells[0][0];
        current.visited = true;
        do {
            next = getNeighbour(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            } else if (!stack.isEmpty()) {
                current = stack.pop();
            }
        } while (!stack.isEmpty());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);

        int width = getWidth();
        int height = getHeight();

        if (width / height < COLS / ROWS)
            cellSize = width / (COLS + 1);
        else
            cellSize = height / (ROWS + 1);

        hMargin = (width - COLS * cellSize) / 2;
        vMargin = (height - ROWS * cellSize) / 2;

        canvas.translate(hMargin, vMargin);

        // Drawing walls for each cell in the maze
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if (cells[x][y].topWall)
                    canvas.drawLine(
                            x * cellSize,
                            y * cellSize,
                            (x + 1) * cellSize,
                            y * cellSize,
                            wallPaint);

                if (cells[x][y].leftWall)
                    canvas.drawLine(
                            x * cellSize,
                            y * cellSize,
                            x * cellSize,
                            (y + 1) * cellSize,
                            wallPaint);

                if (cells[x][y].bottomWall)
                    canvas.drawLine(
                            x * cellSize,
                            (y + 1) * cellSize,
                            (x + 1) * cellSize,
                            (y + 1) * cellSize,
                            wallPaint);

                if (cells[x][y].rightWall)
                    canvas.drawLine(
                            (x + 1) * cellSize,
                            y * cellSize,
                            (x + 1) * cellSize,
                            (y + 1) * cellSize,
                            wallPaint);
            }
        }

        float margin = cellSize / 10;

        canvas.drawRect(
                player.col * cellSize + margin,
                player.row * cellSize + margin,
                (player.col + 1) * cellSize - margin,
                (player.row + 1) * cellSize - margin,
                playerPaint);

        canvas.drawRect(
                exit.col * cellSize + margin,
                exit.row * cellSize + margin,
                (exit.col + 1) * cellSize - margin,
                (exit.row + 1) * cellSize - margin,
                exitPaint);

        // Draw score and game count
        canvas.drawText("Score: " + score, 50, 100, textPaint);
        canvas.drawText("Games: " + gamesPlayed, 50, 200, textPaint);
    }

    private void movePlayer(Direction direction) {
        switch (direction) {
            case UP:
                if (!player.topWall && player.row > 0)
                    player = cells[player.col][player.row - 1];
                break;
            case DOWN:
                if (!player.bottomWall && player.row < ROWS - 1)
                    player = cells[player.col][player.row + 1];
                break;
            case LEFT:
                if (!player.leftWall && player.col > 0)
                    player = cells[player.col - 1][player.row];
                break;
            case RIGHT:
                if (!player.rightWall && player.col < COLS - 1)
                    player = cells[player.col + 1][player.row];
                break;
        }
        checkExit();
        invalidate();
    }

    private void checkExit() {
        if (player == exit) {
            score++;
            gamesPlayed++;
            databaseHelper.addMazeGameResult(score); // Add score to the database
            createMaze();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();

            float playerCenterX = hMargin + (player.col + 0.5f) * cellSize;
            float playerCenterY = vMargin + (player.row + 0.5f) * cellSize;

            float dx = x - playerCenterX;
            float dy = y - playerCenterY;

            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);

            if (absDx > cellSize || absDy > cellSize) {
                if (absDx > absDy) {
                    if (dx > 0)
                        movePlayer(Direction.RIGHT);
                    else
                        movePlayer(Direction.LEFT);
                } else {
                    if (dy > 0)
                        movePlayer(Direction.DOWN);
                    else
                        movePlayer(Direction.UP);
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class Cell {
        boolean topWall = true, leftWall = true, bottomWall = true, rightWall = true, visited = false;
        int col, row;

        public Cell(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }
}
