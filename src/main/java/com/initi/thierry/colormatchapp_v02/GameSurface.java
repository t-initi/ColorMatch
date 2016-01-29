package com.initi.thierry.colormatchapp_v02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

/**
 * Created by Thierry on 11/25/2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable, View.OnTouchListener {
    /** Constantes des couleurs**/
    private final int CASE_VIDE =0;
    private final int CASE_MARRON =1;
    private final int CASE_JAUNE =2;
    private final int CASE_BLEUE =3;
    private final int CASE_GRISE =4;
    private final int CASE_VIOLET =5;
    private final int CASE_ROUGE =6;
    private final int CASE_BLEU_CIEL =7;
    private final int CASE_VERTE =8;

    /** Variable representant la taille d'une **/
    private int squareLength;
    /** variable ddes points du joueur**/
    private int score=0;
    private Bitmap gameOver;
    /** Thread du dessin**/
    Thread thread1 =null;

    SurfaceHolder holder;
    /** Boolean qui verifie si le jeu est acive**/
    private boolean on = false;
    private boolean timeIsUp = false;
    /** Declaration et initilaision du tableau de reference des cases**/
    private int [][]carreauRef = new int[14][10];
    /** Holorge du jeu **/
    private Horloge clock;
    /** Taille de l'ecran **/
    private int screenWidth;
    /** temps restant pour finir le jeu **/
    int timeLeft;
    Context appContext;

    public GameSurface(Context context){
        super(context);
        appContext = context;
        holder =  getHolder();
        holder.addCallback(this);

    }
    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        appContext = context;
        holder =  getHolder();
        holder.addCallback(this);

    }
    public GameSurface(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        appContext = context;
        holder =  getHolder();
        holder.addCallback(this);
    }


    public void init(){
        on =true;
        loadBoard();
        score =0;
        clock = new Horloge();
    }


    public void loadBoard(){
        for(int j=0;j<14;j++){
            for(int i=0; i<10;i++){
                Random r = new Random();
                carreauRef[j][i]= r.nextInt(8);//Math.random()
            }
        }
    }

    /***
     * Dessine le tableau initial sans les pionts
     * @param canvas
     */
    public void drawInitialBoard(Canvas canvas){
        Paint greyPaint =new Paint();
        greyPaint.setColor(Color.rgb(192,192,192));
        int topMargin = squareLength;
        //int y =6;
        int y = topMargin;
        while(y <= ((squareLength*2) * 7)){
            for(int x=0; x<=squareLength*9; x +=squareLength*2){
                canvas.drawRect(new Rect(x,y,x+squareLength,y+squareLength), greyPaint);
            }
            y += (squareLength * 2);
        }
        int y2= topMargin +squareLength;
        while(y2 <= ((squareLength*2) * 7)){
            for(int x2=squareLength; x2<squareLength*10; x2+=squareLength*2){
                canvas.drawRect(new Rect(x2, y2, x2 + squareLength, y2 + squareLength), greyPaint);
            }
            y2 += (squareLength * 2);
        }
    }

    /***
     * Dessine les carreaux colores sur le tableau
     * @param canvas
     */
    public void drawSquaresOnBoard(Canvas canvas){
        Paint paint =new Paint();
        Random r = new Random();
        int topMargin = squareLength+2;
        int j=0;
        //int k=6;
        int k=topMargin;
        while(j<14){
            for(int i=0; i<10;i++){
                int val = carreauRef[j][i]; // =r.nextInt(9) ;
                switch (val){
                    case CASE_VIDE: paint.setColor(Color.TRANSPARENT);
                        break;
                    case CASE_MARRON: paint.setColor(Color.rgb(190, 132, 128)); //Marron

                        break;
                    case CASE_JAUNE: paint.setColor(Color.YELLOW); //Jaune
                        break;
                    case CASE_BLEUE: paint.setColor(Color.BLUE); //Bleu
                        break;
                    case CASE_GRISE: paint.setColor(Color.rgb(135,154,189)); //Gris
                        break;
                    case CASE_VIOLET: paint.setColor(Color.rgb(247,28,229)); //Violet
                        break;
                    case CASE_ROUGE: paint.setColor(Color.RED); // Rouge
                        break;
                    case CASE_BLEU_CIEL: paint.setColor(Color.rgb(105,224,228)); //Bleu Ciel
                        break;
                    case CASE_VERTE: paint.setColor(Color.rgb(79,203,93));
                        break;
                }
                canvas.drawRoundRect(new RectF((i * squareLength) + 2, k + 2, ((i * squareLength) + squareLength) - 4, (k + squareLength) - 4), 13, 13, paint);
            }
            j++;
            k+= squareLength;
        }
    }

    //Verifie si le tableau est vide
    public boolean isBoardEmpty(){
        boolean resp = true;
        for(int i=0; i<14;i++){
            for(int j=0; j<10;j++){
                if(carreauRef[i][j] != CASE_VIDE){
                    resp = false;
                    break;
                }
            }
        }
        return resp;
    }


    public boolean isTherePossibility(){
        boolean response = false;
        int colors[] = new int[9];
        for(int i=0; i<9;i++){ colors[i] = 0;}

        for(int i=0; i<14;i++){
            for(int j=0; j<10;j++){
                if(carreauRef[i][j]==CASE_VIDE){
                    int xTmp = j;
                    int yTmp = i;
                    //Look Left
                    if(xTmp-1 >=0){
                        for(int k=xTmp-1 ; k>=0 ; k--){
                            if( carreauRef[yTmp][k] != CASE_VIDE){
                                int valLeft = carreauRef[yTmp][k];
                                colors[valLeft] = colors[valLeft]+ 1;
                                break;
                            }
                        }
                    }

                    //Look right
                    if(xTmp+1 <10){
                        for(int k=xTmp+1 ; k<10 ; k++){
                            if( carreauRef[yTmp][k] != 0){
                                int valRight = carreauRef[yTmp][k];
                                colors[valRight] = colors[valRight]+ 1;
                                break;
                            }
                        }
                    }

                    //Look Up
                    if(yTmp-1 >=0){
                        for(int l=yTmp-1 ; l>=0 ; l--){
                            if( carreauRef[l][xTmp] != 0){
                                int valUp = carreauRef[l][xTmp];
                                colors[valUp] = colors[valUp]+ 1;
                                break;
                            }
                        }
                    }

                    //Look Down
                    if(yTmp+1 <14){
                        for(int l=yTmp+1 ; l<14 ; l++){
                            if( carreauRef[l][xTmp] != 0){
                                int valDown = carreauRef[l][xTmp];
                                colors[valDown] = colors[valDown ]+ 1;
                                break;
                            }
                        }
                    }


                }
            }

            for(int m=1; m<9;m++){
                //System.out.println("Values found "+i+" : "+colors[i]);
                if(colors[m] > 1){
                    response  = true;
                    //System.out.println("Possible");
                    break;
                }
            }
        }
        return response;
    }

    /**
     * Met a jour le tableau et ses element selon la position y et x
     * @param xIndice
     * @param yIndice
     */
    public void gameCalculator(int xIndice, int yIndice){
        System.out.println("********Game processing **********");
        int xTabPos, yTabPos, xTmp,yTmp;
        Point[]points = new Point[4] ;
        int colors[] = new int[9];
        for(int i=0; i<9;i++){ colors[i] = 0;}

        int totalFound =0;
        int x = xIndice;
        int y = yIndice;

        xTabPos= x/squareLength;
        yTabPos = (y-squareLength)/squareLength;
        if(xTabPos < 10 && yTabPos <14){


            int val = carreauRef[yTabPos][xTabPos];

            xTmp = xTabPos;
            yTmp = yTabPos;
            System.out.println("Touch position " + xTabPos + " ," + yTabPos + " Value = " + carreauRef[yTabPos][xTabPos]);
            if(val==CASE_VIDE){
                //Look Left
                if(xTmp-1 >=0){
                    for(int i=xTmp-1 ; i>=0 ; i--){
                        if( carreauRef[yTabPos][i] != CASE_VIDE){
                            int valLeft = carreauRef[yTabPos][i];
                            points[totalFound] = new Point(i,yTabPos);
                            //System.out.println("Value found on left side "+valLeft);
                            colors[valLeft] = colors[valLeft]+ 1;
                            totalFound++;
                            break;
                        }
                    }
                }
                //Look right
                if(xTmp+1 <10){
                    for(int i=xTmp+1 ; i<10 ; i++){
                        if( carreauRef[yTabPos][i] != 0){
                            int valRight = carreauRef[yTabPos][i];
                            points[totalFound] = new Point(i,yTabPos);
                            //System.out.println("Value found on right side "+valRight);
                            colors[valRight] = colors[valRight]+ 1;
                            totalFound ++;
                            break;
                        }
                    }
                }

                //Look Up
                if(yTmp-1 >=0){
                    for(int j=yTmp-1 ; j>=0 ; j--){
                        if( carreauRef[j][xTabPos] != 0){
                            int valUp = carreauRef[j][xTabPos];
                            points[totalFound] = new Point(xTabPos,j);
                            //System.out.println("Value found on top side "+valUp);
                            colors[valUp] = colors[valUp]+ 1;
                            totalFound++;
                            break;
                        }
                    }
                }

                //Look Down
                if(yTmp+1 <14){
                    for(int j=yTmp+1 ; j<14 ; j++){
                        if( carreauRef[j][xTabPos] != 0){
                            int valDown = carreauRef[j][xTabPos];
                            points[totalFound] = new Point(xTabPos,j);
                            //System.out.println("Value found on down side "+valDown);
                            colors[valDown] = colors[valDown ]+ 1;
                            totalFound ++;
                            break;
                        }
                    }
                }
                //Les nombre de valeur trouve
                int toRemove[] = new int [2];
                int cpt=0;
                int nbTrouve = 0;
                for(int i=1; i<9;i++){

                    //System.out.println("Values found "+i+" : "+colors[i]);
                    if(colors[i] > 1){
                        nbTrouve += colors[i];
                        toRemove[cpt] =i;
                        cpt++;
                    }
                }
                //Mise a jour du score
                if(nbTrouve ==2){
                    score = score +20;

                }else if(nbTrouve ==3){
                    score = score +60;

                }else if(nbTrouve ==4){
                    score = score +120;

                }

                int found = toRemove.length;
                System.out.println("nb to remove "+cpt);
                if(found ==1){
                    for(int i=0; i<totalFound;i++){
                        int xx = points[i].getX();
                        int yy = points[i].getY();
                        if(carreauRef[yy][xx]==toRemove[0]){
                            // Animation avant de supprimer la couleur

                            //On supprime le couleur
                            carreauRef[yy][xx] =0;
                        }
                    }
                }else if(found ==2){
                    for(int i=0; i<totalFound;i++){
                        int xx = points[i].getX();
                        int yy = points[i].getY();
                        if(carreauRef[yy][xx]==toRemove[0] || carreauRef[yy][xx]==toRemove[1]){
                            //System.out.println("i2: "+i+"  x="+xx+" y="+yy+"  "+carreauRef[xx][yy]);
                            //On supprime le couleur
                            carreauRef[yy] [xx]=0;
                        }
                    }
                }

            }else{
                System.out.println("Le carré n'est pas vide.");
            }
        }
    }

    /**
     * Dessine le score du joueur
     * @param c
     */
    public void drawScore(Canvas c){
        int xPos = screenWidth - (squareLength * 3);
        String tmpScore = "" + score + "";
        Paint pt = new Paint();

        pt.setColor(Color.BLACK);
        pt.setStyle(Paint.Style.FILL); //
        pt.setTextSize((float) squareLength);
        c.drawText(tmpScore, xPos, squareLength - 8, pt);

        pt.setStrokeWidth(5);
        pt.setColor(Color.WHITE);
        pt.setStyle(Paint.Style.STROKE);
        c.drawText(tmpScore, xPos, squareLength - 8, pt);
    }

    public void drawGameOver(Canvas canvas){
        Paint pt = new Paint();

        gameOver = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.game_over2);

        pt.setStyle(Paint.Style.FILL);
        pt.setColor(Color.BLACK);
        pt.setTextSize(130);
        canvas.drawText(score + "", squareLength*2, squareLength * 10, pt);
        pt.setStyle(Paint.Style.STROKE);
        pt.setColor(Color.RED);
        canvas.drawBitmap(gameOver, 0, 0, pt);
        canvas.drawText(score + "", squareLength*2, squareLength * 10, pt);


    }

    public void drawProgressBar(Canvas canvas){
        int toReduce = screenWidth/(85);
        int timeBarWidth = screenWidth - (toReduce *(timeLeft));
        Paint pt = new Paint();

        RectF rectF = new RectF(0,0,screenWidth,squareLength-4);


        pt.setStyle(Paint.Style.FILL);
        pt.setColor(Color.RED);
        canvas.drawRoundRect(rectF, 13, 13, pt);

        //Border
        pt.setStyle(Paint.Style.STROKE);
        pt.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, 13, 13, pt);

        //Second Bar
        Paint pt2 = new Paint();
        RectF rec2 = new RectF(0,0,timeBarWidth,squareLength-4);

        pt2.setStyle(Paint.Style.FILL);
        pt2.setColor(Color.rgb(0, 250, 0));
        canvas.drawRoundRect(rec2, 13, 13, pt2);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init();
        thread1 = new Thread(this);
        thread1.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            squareLength = width/ 10;
            screenWidth = width;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void pause(){
        on = false;
        while(true){
            try {
                thread1.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("-> RUN <-", "PB DANS JOINING THREAD ON PAUSE");
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                //if(x>=0 && x<=squareLength*17 && y>=0 && y<=squareLength*10){
                    gameCalculator((int) event.getX(), (int) event.getY());
                //}

                Log.i("-> RUN <-", "TOUCHED  "+event.getX()+" "+event.getY());
                break;
        }
        return true;
    }

    private class Horloge{
        CountDownTimer timer = new CountDownTimer(90000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (90) - (int)millisUntilFinished/1000;
                int tmps = (int ) millisUntilFinished/1000;
                //if(tmps % 30 ==0 )
                    //System.out.println("Time remaining "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                saveInformation();
                timeIsUp = true;

            }
        }.start();
    }


    public void saveInformation(){
        Date date = new Date();

        String dateStr =date.getDay()+"/"+date.getMonth()+"/2016  "+date.getHours()+":"+date.getMinutes();
        DatabaseOperations dop = new DatabaseOperations(appContext);
        dop.insertInformation(dop,"MainPlayer",dateStr,score);
    }

    public void drawAll(Canvas c){
        c.drawRGB(255, 255, 255);
        if(!timeIsUp){
            drawInitialBoard(c);
            drawSquaresOnBoard(c);
            drawProgressBar(c);
            drawScore(c);
            if(isBoardEmpty()){
                Log.i("Board empty","The board is empty!");
                loadBoard();
                clock.timer.start();
            }
            if(!isTherePossibility()){
                Log.i("Board ","No possiblity");
                loadBoard();
                clock.timer.start();
            }
        }else if(timeIsUp){
            drawGameOver(c);

            //on = false;
        }

    }

    @Override
    public void run() {
        Canvas c = null;
        while(on){
            if(!holder.getSurface().isValid()){
                Log.e("-> RUN <-", "PB DANS SURFACE");
                continue;
            }

            try {
                thread1.sleep(40);
                c = holder.lockCanvas(null);
                try {
                    drawAll(c);
                    holder.unlockCanvasAndPost(c);
                }finally {
                   /* if(c ==null){
                        holder.unlockCanvasAndPost(c);
                    }*/

                }

            } catch (Exception e) {

                Log.e("-> RUN <-", "PB DANS RUN");
                //((Activity)appContext).finish();
                //Intent intent = new Intent("kill");
                //LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }


        }



    }


    //protected class SyncThread
}
