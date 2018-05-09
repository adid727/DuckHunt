package com.project.AdidNissan.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);
        /*Button goBack;
        goBack = (Button)findViewById(R.id.goBack);
        goBack.setOnClickListener((View.OnClickListener) this);*/
        final Button button = (Button) findViewById(R.id.goback);


        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);
        int BestTime = settings.getInt("BEST TIME", 0);

        if(score > highScore){
            highScoreLabel.setText("High Score: " + score);

            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();

        }else{
            highScoreLabel.setText("High Score: " + highScore);
        }


    }


    public void clicked(View view) {
        // Perform action on click
        Intent i = new Intent(result.this, Menu.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
