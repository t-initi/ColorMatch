package com.initi.thierry.colormatchapp_v02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageButton play_bt,score_bt,muteBt;
    private boolean musicOn = false;

    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play_bt = (ImageButton)findViewById(R.id.play_bt);
        score_bt = (ImageButton)findViewById(R.id.sore_bt);
        muteBt = (ImageButton)findViewById(R.id.mute_bt);
        play_bt.setOnClickListener(this);
        score_bt.setOnClickListener(this);
        muteBt.setOnClickListener(this);
        mp = MediaPlayer.create(this, R.raw.bg_music);

    }


    /*
    @Override
    protected void onStop() {
        super.onStop();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_bt:
                Intent startGameActivity = new Intent(this, GameActivity.class);
                startActivity(startGameActivity);
                //Toast.makeText(this, "Game loaded !", Toast.LENGTH_LONG).show();
               //finish();
                break;
            case R.id.sore_bt:
                Intent startResultActivity = new Intent(this, GameResultActivity.class);
                startActivity(startResultActivity);
               //finish();
                break;
            case R.id.mute_bt:
                if(musicOn==false){
                    mp.start();
                    muteBt.setImageResource(R.drawable.mute_on_min);
                    musicOn =true;
                }else{
                    mp.stop();
                    musicOn =false;
                    muteBt.setImageResource(R.drawable.mute_off_min);

            }
                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
