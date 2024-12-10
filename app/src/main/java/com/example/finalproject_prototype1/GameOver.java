package com.example.finalproject_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameOver extends AppCompatActivity {

    private TextView yourScore;
    private Button playAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);

        //initialise TextView and buttons
        yourScore = findViewById(R.id.TV_YourScore);
        playAgain = findViewById(R.id.btn_PlayAgain);

        //accept score parameter from GamePlay activity
        int score = getIntent().getIntExtra("score",0);

        //display the score
        yourScore.setText(String.valueOf(score)); // must convert to string

        //play again button listener
        playAgainBtn();
    }
    private void playAgainBtn(){
        playAgain.setOnClickListener(v->{
            Intent pGP = new Intent(GameOver.this, GamePlay.class);
            startActivity(pGP);
            finish();//close the GameOver activity
        });
    }
}