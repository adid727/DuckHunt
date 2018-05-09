package com.project.AdidNissan.finalproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Main extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent myLocalIntent = getIntent();
        int speed = myLocalIntent.getIntExtra("SPEED", 1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new GameView(this, speed));

    }
}
