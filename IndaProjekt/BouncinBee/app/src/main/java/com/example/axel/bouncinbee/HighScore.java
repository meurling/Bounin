package com.example.axel.bouncinbee;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        TextView highScoreLabel = (TextView) findViewById(R.id.HighScorePage);

        int highScore = settings.getInt("HIGH_SCORE", 0);

        highScoreLabel.setText(""+highScore);
    }


}
