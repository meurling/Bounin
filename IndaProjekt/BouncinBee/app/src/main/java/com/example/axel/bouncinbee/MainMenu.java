package com.example.axel.bouncinbee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by axel on 2017-04-11.
 */

public class MainMenu extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    // Start game on click
    public void onClickStartGame(View v){
        setContentView(new GamePanel(this));
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void goHighScore(View v){
        startActivity(new Intent(getApplicationContext(), HighScore.class));
    }
}
