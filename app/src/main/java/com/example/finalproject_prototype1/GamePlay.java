package com.example.finalproject_prototype1;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlay extends AppCompatActivity {

    private ImageButton yellowWool, redWool, greenWool, blueWool;
    private TextView countDown;
    private List<ImageButton> buttonList; // List of wool buttons
    private List<Integer> sequence; // Random sequence of indices
    private List<Integer> userInput; // User input sequence
    private Handler handler = new Handler(); // Handler for delayed tasks
    private Random random = new Random(); // Random generator
    private int score = 0; // Track the score
    private int sequenceLength = 4; // level 1 is 4 colours

    //testing accelerometer
    private TextView tvAccelerometer;
    // Fields for the accelerometer
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private SensorEventListener sensorEventListenerAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_play);

        //initialise sensor manager
        //testing accelerometer
        tvAccelerometer = findViewById(R.id._tv_test_Acc);
        // Initialize SensorManager and Accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialise buttons and TextView
        yellowWool = findViewById(R.id.IV_yellowWool);
        redWool = findViewById(R.id.IV_RedWool);
        greenWool = findViewById(R.id.IV_GreenWool);
        blueWool = findViewById(R.id.IV_BlueWool);
        countDown = findViewById(R.id.tv_CountDown);

        // Add Image buttons to the list in the same order as the sequence
        buttonList = new ArrayList<>();
        buttonList.add(redWool); // 0 - Red
        buttonList.add(yellowWool); // 1 - Yellow
        buttonList.add(greenWool); // 2 - Green
        buttonList.add(blueWool); // 3 - Blue

        // Display game countdown using Runnable to wait for methods to complete
        displayCountDown(() -> {
            generateRandomSequence(4);
            displaySequence(this::enableUserInput);
        });

        //Accelerometer listener
        if (sensorAccelerometer != null) {
            sensorEventListenerAcc = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    tvAccelerometer.setText(String.format("x: %.2f, y: %.2f, z: %.2f", x, y, z));

                    //testing
                    //neutral =x: 9.60, y: -0.54, z: 0.79
                    //Forward =x: 1.52, y: -0.16, z: 9.34
                    //Backward =x: 3.38, y: -0.38, z:9.64
                    //left = x: 9.76, y: -0.65, z: 0.82
                    //right =x: 9.78, y: 0.16, z: 0.85

                    // Example logic for tilt direction
                    if(z>8){
                        if (x<2.5) {
                            // Phone tilted forward
                            handleTilt("forward");
                        } else if (x > 2.5 && x<8) {
                            // Phone tilted backward
                            handleTilt("backward");
                        }
                    }
                    else{
                        if (y>0) {
                            // Phone tilted right
                            handleTilt("right");
                        } else if (y<0) {
                            // Phone tilted left
                            handleTilt("left");
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Handle accuracy changes if needed
                }
            };
        } else {
            Toast.makeText(this, "Accelerometer not available!", Toast.LENGTH_SHORT).show();
        }
    }
    // Handle phone tilt
    private void handleTilt(String direction) {
        Toast.makeText(this, "Tilt: " + direction, Toast.LENGTH_SHORT).show();
    }

    // Register and unregister the listener to save battery
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorAccelerometer != null) {
            sensorManager.registerListener(sensorEventListenerAcc, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorAccelerometer != null) {
            sensorManager.unregisterListener(sensorEventListenerAcc);
        }
    }

    // Display a countdown before the game starts
    private void displayCountDown(Runnable onComplete) {
        int countdownDuration = 4000; // Total countdown duration
        handler.postDelayed(() -> countDown.setText("3"), 0);
        handler.postDelayed(() -> countDown.setText("2"), 1000);
        handler.postDelayed(() -> countDown.setText("1"), 2000);
        // Hide the countdown text after the countdown is done
        handler.postDelayed(() -> {
            countDown.setVisibility(View.INVISIBLE);
            onComplete.run(); // Execute the next task after countdown
        }, 4000);
    }
    // Generate a random sequence of indices
    private void generateRandomSequence(int length) {
        sequence = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            sequence.add(random.nextInt(buttonList.size())); // Random index for highlight wool
        }
    }
    // Display the sequence to the user
    private void displaySequence(Runnable onComplete) {
        for (int i = 0; i < sequence.size(); i++) {
            final int index = sequence.get(i); // Get the wool index of the button
            final ImageButton button = buttonList.get(index); // Get the button to highlight
            handler.postDelayed(() -> {
                button.setImageResource(R.drawable.whitewool); // Show highlight by setting source
                handler.postDelayed(() -> button.setImageResource(
                        getWoolDrawable(button)), 500); // Revert to original source
            }, i * 2000); // Highlight with a delay
        }
        // Call the onComplete Runnable after the sequence finishes
        handler.postDelayed(onComplete, sequence.size() * 2000);
    }
    // Enable user input by setting click listeners
    private void enableUserInput() {
        //prompt user to input
        countDown.setVisibility(View.VISIBLE);
        countDown.setText("COPY!");
        handler.postDelayed(() -> countDown.setVisibility(View.INVISIBLE), 2000);
        // add click listeners to the buttons
        userInput = new ArrayList<>();
        for (int i = 0; i < buttonList.size(); i++) {
            final int index = i; // Capture the index for comparison
            ImageButton button = buttonList.get(i);
            button.setOnClickListener(v -> {
                userInput.add(index); // Add user input
                validateUserInput(); // Check the input after each click
            });
        }
    }
    // Validate the user's input against the sequence
    private void validateUserInput() {
        if (userInput.size() <= sequence.size()) {
            if (userInput.get(userInput.size() - 1).equals(sequence.get(userInput.size() - 1))) {
                if (userInput.size() == sequence.size()) {
                    countDown.setVisibility(View.VISIBLE);
                    countDown.setText("You Won!");
                    //update score
                    score += sequenceLength;
                    disableUserInput(); // Disable further input
                    prepareNextRound();
                }
            } else {
                countDown.setVisibility(View.VISIBLE);
                countDown.setText("You Lost!");
                disableUserInput(); // Disable further input
                handler.postDelayed(this::navigateToGameOver,2000); // Navigate to game over
            }
        }
    }
    // Get the original drawable resource for a button
    private int getWoolDrawable(ImageButton button) {
        if (button == redWool) return R.drawable.redwool;
        if (button == yellowWool) return R.drawable.yellowwool;
        if (button == greenWool) return R.drawable.greenwool;
        if (button == blueWool) return R.drawable.bluewool;
        return 0; // Default, should not occur
    }
    // Disable user input by removing click listeners
    private void disableUserInput() {
        for (ImageButton button : buttonList) {
            button.setOnClickListener(null); // Remove click listener
        }
    }
    // Prepare the next round of the game
    private void prepareNextRound() {
        handler.postDelayed(() -> {
            sequenceLength += 2; // Increase sequence length by 2
            displayCountDown(() -> {
                //start next round with updated sequence
                generateRandomSequence(sequenceLength);
                displaySequence(this::enableUserInput);
            });
        }, 2000); // Delay before starting the next round
    }
    // Navigate to the GameOver screen and pass score
    private void navigateToGameOver() {
        try {
            Intent pGO = new Intent(GamePlay.this, GameOver.class);
            pGO.putExtra("score", score); // Pass the score to GameOver activity
            startActivity(pGO);
            finish(); // End the current GamePlay activity
        } catch (Exception e) {
            e.printStackTrace(); // Log any errors
        }
    }

}