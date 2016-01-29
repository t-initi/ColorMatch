package com.initi.thierry.colormatchapp_v02;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;

public class GameActivity extends Activity  implements View.OnTouchListener{
    GameSurface gs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gs = (GameSurface)findViewById(R.id.GameSurface);
        gs.setOnTouchListener(this);
        gs.setVisibility(View.VISIBLE);
        gs.setFocusable(true);
       // setContentView(gs);

        LocalBroadcastManager.getInstance(this).registerReceiver(new MessageHandler(),
                new IntentFilter("kill"));


    }

    @Override
    protected void onPause(){
        super.onPause();
        //gs.pause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gs.onTouch(v,event);
        return true;
    }

    private void killActivity() {
        finish();
    }
    public void setEndScreen(){

    }

    public class MessageHandler extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            killActivity();
        }
    }
}

