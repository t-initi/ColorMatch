package com.initi.thierry.colormatchapp_v02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.initi.thierry.colormatchapp_v02.HighScoreData.TableInfo;

/**
 * Created by Thierry on 1/1/2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {
    public final static int databaseVersion = 1;
    public String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS "+ HighScoreData.TableInfo.TABLE_NAME +
            "("+ TableInfo.PLAYER_NAME+" TEXT, "+ TableInfo.SCORE_DATE+" TEXT , "+ TableInfo.PLAYER_SCORE+" INT, " +
            "PRIMARY KEY ("+TableInfo.SCORE_DATE+") );";

    public DatabaseOperations(Context context) {
        super(context, HighScoreData.TableInfo.DATABASE_NAME, null, databaseVersion);
        Log.d("Database Operation "," Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database Operation ", " Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertInformation(DatabaseOperations dop, String name, String date, int score){
        SQLiteDatabase sDB = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.SCORE_DATE,date);
        cv.put(TableInfo.PLAYER_NAME,name);
        cv.put(TableInfo.PLAYER_SCORE, score);

        long k = sDB.insert(TableInfo.TABLE_NAME,null,cv);
        Log.d("Database Operation "," One row inserted");
    }

    public Cursor selectInformation(DatabaseOperations dop){
        SQLiteDatabase sDB = dop.getReadableDatabase();
        String[]columns = {TableInfo.PLAYER_NAME,TableInfo.PLAYER_SCORE,TableInfo.SCORE_DATE};
        Cursor cursor = sDB.query(TableInfo.TABLE_NAME,columns,null,null,null,null,TableInfo.PLAYER_SCORE+" DESC");

        return cursor;
    }
}
