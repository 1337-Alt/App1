package com.example.thecontest;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TheContest";
    private static final int MAX_SCORE = 15;

    private int score = 0;
    private TextView scoreLabel, scoreValue;
    private Button btnAdd, btnSubtract, btnRestart;
    private MediaPlayer victorySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Activity created");

        // Initialize UI elements
        scoreLabel = findViewById(R.id.label_score);
        scoreValue = findViewById(R.id.value_score);
        btnAdd = findViewById(R.id.btn_add);
        btnSubtract = findViewById(R.id.btn_subtract);
        btnRestart = findViewById(R.id.btn_restart);

        // Load victory sound
        victorySound = MediaPlayer.create(this, R.raw.victory_sound);

        // Restore score if activity is recreated
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("SCORE_KEY", 0);
            updateScoreDisplay();
        }

        // Set button click listeners
        btnAdd.setOnClickListener(v -> updateScore(1));
        btnSubtract.setOnClickListener(v -> updateScore(-1));
        btnRestart.setOnClickListener(v -> resetScore());
    }

    /**
     * Updates the score based on the given delta.
     * Prevents the score from exceeding MAX_SCORE or dropping below 0.
     */
    private void updateScore(int delta) {
        if (score == MAX_SCORE && delta > 0) {
            Log.i(TAG, "Score cannot exceed " + MAX_SCORE);
            return;
        }

        score = Math.max(0, Math.min(MAX_SCORE, score + delta));
        updateScoreDisplay();

        if (score == MAX_SCORE) {
            Log.i(TAG, "Player reached the winning score: " + MAX_SCORE);
            playVictorySound();
            scoreValue.setTextColor(Color.GREEN);
            btnAdd.setEnabled(false); // Disable the Add button
            btnSubtract.setEnabled(false); // Disable the Subtract button
        } else if (score < MAX_SCORE) {
            scoreValue.setTextColor(Color.BLACK);
            btnAdd.setEnabled(true); // Enable the Add button
            btnSubtract.setEnabled(true); // Enable the Subtract button
        }
    }

    /**
     * Resets the score to 0, changes text color to blue, and re-enables buttons.
     */
    private void resetScore() {
        score = 0;
        updateScoreDisplay();
        scoreValue.setTextColor(Color.BLUE);
        btnAdd.setEnabled(true);
        btnSubtract.setEnabled(true);
        Log.i(TAG, "Game restarted. Score reset.");
    }

    /**
     * Updates the score displayed in the TextView.
     */
    private void updateScoreDisplay() {
        scoreValue.setText(String.valueOf(score));
        Log.i(TAG, "Score updated to: " + score);
    }

    /**
     * Plays the victory sound when the score reaches MAX_SCORE.
     */
    private void playVictorySound() {
        if (victorySound != null && !victorySound.isPlaying()) {
            victorySound.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SCORE_KEY", score);
        Log.d(TAG, "Score saved: " + score);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (victorySound != null) {
            victorySound.release();
        }
        Log.i(TAG, "Media resources released.");
    }
}
