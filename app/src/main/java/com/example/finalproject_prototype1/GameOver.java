package com.example.finalproject_prototype1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameOver extends AppCompatActivity {

    private TextView yourScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);

        //initialise TextView
        yourScore = findViewById(R.id.TV_YourScore);

        //accept score parameter from GamePlay activity
        int score = getIntent().getIntExtra("score",0);

        //display the score
        yourScore.setText(score);
    }
}