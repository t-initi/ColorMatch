package com.initi.thierry.colormatchapp_v02;

import android.provider.BaseColumns;

/**
 * Created by Thierry on 1/1/2016.
 */
public class HighScoreData {

    public HighScoreData(){

    }


    public static abstract class TableInfo implements BaseColumns{
        public final static String PLAYER_NAME = "player_name";
        public final static String SCORE_DATE = "score_date";
        public final static String PLAYER_SCORE = "player_score";
        public final static String DATABASE_NAME = "color_match_db";
        public final static String TABLE_NAME = "highscore_info";

    }


}
