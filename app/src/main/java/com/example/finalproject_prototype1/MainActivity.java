package com.example.finalproject_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //initialise UI Elements
        btnPlay= findViewById(R.id.btn_Play);

        //button click to go to game play activity
        btnPlay.setOnClickListener(this::onButtonClick);
    }
    public void onButtonClick(View view){
        Intent GP =new Intent(view.getContext(), GamePlay.class);
        startActivity(GP);
    }
}