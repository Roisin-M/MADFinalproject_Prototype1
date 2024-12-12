package com.example.finalproject_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class HighScores extends AppCompatActivity {

    private TextView highScoresList;
    private DatabaseHandler db;
    private Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_scores);

        db = new DatabaseHandler(this);
        highScoresList = findViewById(R.id.tv_HighScoresList);
        goBack = findViewById(R.id.btn_NavGameOver);
        displayTopScores();

        //method to go back to navigate back to Main Screen
        GoBack();

    }
    private void GoBack(){
        goBack.setOnClickListener(v->{
            Intent pMA = new Intent(HighScores.this, MainActivity.class);
            startActivity(pMA);
            finish();
        });
    }
    private void displayTopScores() {
        List<HighScoreClass> topScores = db.top5Highscore();

        StringBuilder scoresText = new StringBuilder("Top 5 Scores:\n\n");
        for (HighScoreClass score : topScores) {
            scoresText.append(score.getName()).append(": ")
                    .append(score.getHighscore()).append("\n");
        }
        highScoresList.setText(scoresText.toString());
    }
}