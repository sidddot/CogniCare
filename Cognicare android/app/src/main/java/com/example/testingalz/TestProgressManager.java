package com.example.testingalz;

import java.util.ArrayList;
import java.util.List;

public class TestProgressManager {

    private static TestProgressManager instance;
    private List<Integer> nbackResults;
    private List<Integer> memoryUpdatingResults;
    private List<Integer> corsiBlockResults;

    private TestProgressManager() {
        nbackResults = new ArrayList<>();
        memoryUpdatingResults = new ArrayList<>();
        corsiBlockResults = new ArrayList<>();
    }

    public static synchronized TestProgressManager getInstance() {
        if (instance == null) {
            instance = new TestProgressManager();
        }
        return instance;
    }

    public void addNbackResult(int score) {
        nbackResults.add(score);
    }

    public List<Integer> getNbackResults() {
        return nbackResults;
    }

    public void addMemoryUpdatingResult(int score) {
        memoryUpdatingResults.add(score);
    }

    public List<Integer> getMemoryUpdatingResults() {
        return memoryUpdatingResults;
    }

    public void addCorsiBlockResult(int score) {
        corsiBlockResults.add(score);
    }

    public List<Integer> getCorsiBlockResults() {
        return corsiBlockResults;
    }

    public void resetAll() {
        nbackResults.clear();
        memoryUpdatingResults.clear();
        corsiBlockResults.clear();
    }
}
