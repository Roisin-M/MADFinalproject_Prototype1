package com.example.finalproject_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameOver extends AppCompatActivity {

    private TextView yourScore;
    private int score;
    private Button playAgain, leaderBoard;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);

        //initialise databasehandler
        db = new DatabaseHandler(this);

        //initialise TextView and buttons
        yourScore = findViewById(R.id.TV_YourScore);
        playAgain = findViewById(R.id.btn_PlayAgain);
        leaderBoard = findViewById(R.id.btn_LeaderBoard);

        //accept score parameter from GamePlay activity
        score = getIntent().getIntExtra("score",0);

        //display the score
        yourScore.setText(String.valueOf(score)); // must convert to string

        //Check if score is in the top 5 and prompt for a name
        checkTop5AndPrompt();

        //play again button listener
        playAgainBtn();

        //leaderboard button Listener
        leaderBoardBtn();
    }
    private void checkTop5AndPrompt(){
        if(isScoreInTop5(score)){
            //shwo dialog box
            showNameInputDialog();
        }
    }
    private boolean isScoreInTop5(int score){
        //get the top 5 scores from db
        return db.top5Highscore().size()<5
                || score > db.top5Highscore()
                .get(db.top5Highscore().size()-1)
                .getHighscore();
    }
    private void showNameInputDialog(){
        // Inflate the custom dialog view
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_name_input, null);
        EditText nameInput = dialogView.findViewById(R.id.ET_NameInput);

        // Create the AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("New High Score!")
                .setMessage("Enter your name to save your score:")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    if (!name.isEmpty()) {
                        // Add the score to the database
                        db.addHighscore(new HighScoreClass(name, score));
                        Toast.makeText(GameOver.this, "Score saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameOver.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
    public void goToHighScores(){
        //navigate to the highscores activity
        Intent pLB = new Intent(GameOver.this, HighScores.class);
        startActivity(pLB);
        finish();
    }
    private void playAgainBtn(){
        playAgain.setOnClickListener(v->{
            Intent pGP = new Intent(GameOver.this, GamePlay.class);
            startActivity(pGP);
            finish();
        });
    }
    private void leaderBoardBtn(){
        leaderBoard.setOnClickListener(v ->{
            goToHighScores();
        });
    }
}