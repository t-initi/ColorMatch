package com.initi.thierry.colormatchapp_v02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameResultActivity extends Activity implements View.OnClickListener{

    private TableRow tr, mainRow;
    private TableLayout tl;
    private TextView scoreView, dateView,nameView;
    Button edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        tl = (TableLayout)findViewById(R.id.res_table);
        mainRow = (TableRow)findViewById(R.id.main_row);

        edt = (Button)findViewById(R.id.edit_name);
        edt.setOnClickListener(this);
        scoreView=(TextView)findViewById(R.id.score_res);
        dateView =(TextView)findViewById(R.id.date_res);
        nameView=(TextView)findViewById(R.id.name_res);

        scoreView.setVisibility(View.INVISIBLE);
        dateView.setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.INVISIBLE);

        tl.setColumnStretchable(0,true);
        tl.setColumnStretchable(1, true);
        tl.setColumnStretchable(2, true);
        getInformation();

    }

    public void getInformation(){
        String name,date;
        int score;
        DatabaseOperations dop = new DatabaseOperations(this);
        Cursor cursor = dop.selectInformation(dop);

        while(cursor.moveToNext()){
            name = cursor.getString(0);
            date = cursor.getString(2);
            score = cursor.getInt(1);
            System.out.println("Score " + score + " Name " + name + " Date" + date);

            //Insertion de nouvelles lignes
            TableRow row = new TableRow(this);
            TextView scoreViewRes, dateViewRes,nameViewRes;
            String col = "#8c9cac";
            scoreViewRes = new TextView(this);
            dateViewRes = new TextView(this);
            nameViewRes = new TextView(this);

            nameViewRes.setBackgroundColor(Color.parseColor(col));
            scoreViewRes.setBackgroundColor(Color.parseColor(col));
            dateViewRes.setBackgroundColor(Color.parseColor(col));

            nameViewRes.setLayoutParams(nameView.getLayoutParams());
            scoreViewRes.setLayoutParams(scoreView.getLayoutParams());
            dateViewRes.setLayoutParams(dateView.getLayoutParams());


            nameViewRes.setText(name);
            scoreViewRes.setText(""+score+"");
            dateViewRes.setText(""+date+"");

            row.addView(nameViewRes);
            row.addView(scoreViewRes);
            row.addView(dateViewRes);
            tl.addView(row);

        }



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_name: //getInformation();
                break;
        }
    }
}

