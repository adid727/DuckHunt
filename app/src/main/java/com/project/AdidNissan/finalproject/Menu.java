package com.project.AdidNissan.finalproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    private int Speed;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mp = MediaPlayer.create(this, R.raw.titlescreen);

        mp.start();
        mp.setLooping(true);
        /*Button goBack;
        goBack = (Button)findViewById(R.id.goBack);
        goBack.setOnClickListener((View.OnClickListener) this);*/
        final Button button = (Button) findViewById(R.id.easy);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(getApplicationContext(), Main.class);

                Speed = 25;
                i.putExtra("SPEED", Speed);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mp.stop();
                startActivity(i);
            }
        });

        final Button button2 = (Button) findViewById(R.id.medium);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(getApplicationContext(), Main.class);
                Speed = 45;
                i.putExtra("SPEED", Speed);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        final Button button3 = (Button) findViewById(R.id.hard);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(getApplicationContext(), Main.class);

                Speed = 80;
                i.putExtra("SPEED", Speed);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
