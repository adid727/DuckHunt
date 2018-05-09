package com.project.AdidNissan.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;


public class GameView extends SurfaceView
{
    public Bitmap bmp,bmpBlood, bmpBG, background, bird, dog, score, shots, birdsGUI, birdIcon, birdDead, bulletIcon;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private SoundPlayer sounds;
    public int shotsLeft;
    public Context c;
    private Intent intent;
    private int MyTimer = 0;
    public int ScreenWidth;
    public int ScreenHeight;
    boolean spriteLoaded = false;
    boolean birdSpriteLoaded = false;
    WindowManager wm;
    Display display;
    public Sprite spritebirdDead;
    public Sprite birdSprite;
    public Sprite dogSprite;
    public Sprite spriteScore;
    public Sprite spriteShots;
    public Sprite spriteBirdsGUI;
    public Sprite spriteBullet;

    //alive bird icons
    public Sprite spriteBirdIcon0;
    public Sprite spriteBirdIcon1;
    public Sprite spriteBirdIcon2;
    public Sprite spriteBirdIcon3;
    public Sprite spriteBirdIcon4;
    public Sprite spriteBirdIcon5;
    public Sprite spriteBirdIcon6;
    public Sprite spriteBirdIcon7;
    public Sprite spriteBirdIcon8;
    public Sprite spriteBirdIcon9;

    //dead bird icons
    public Sprite spriteBirdDead0;
    public Sprite spriteBirdDead1;
    public Sprite spriteBirdDead2;
    public Sprite spriteBirdDead3;
    public Sprite spriteBirdDead4;
    public Sprite spriteBirdDead5;
    public Sprite spriteBirdDead6;
    public Sprite spriteBirdDead7;
    public Sprite spriteBirdDead8;
    public Sprite spriteBirdDead9;

    //bird flapping sound
    MediaPlayer BF;
    MediaPlayer DB;
    MediaPlayer BD;
    MediaPlayer DCEL;
    MediaPlayer DLGH;
    MediaPlayer DW;



    public int currentScore = 0;
    public int birdsKilled = 0;
    public int currentBird = 0;
    private int currentLevel = 1;

    //I use this array of booleans to keep track of the bird killed GUI at the bottom. True means that the bird is alive.
    // False means that the bird is dead. I spawn white bird icon if bird is alive and red bird icon if bird is dead
    public Boolean[] ShotRecord = new Boolean[]{true, true, true, true, true, true, true, true, true, true};

    //an enum that i use to keep track of the gamestates
    public static enum GameState {
        DOGWALKIN, DOGJUMP, BIRDFLYING, BIRDDEAD, GAMEOVER, DOGCELEBRATING, DOGLAUGH
    }

    public GameState gameState;

    Timer timer = new Timer();

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            MyTimer++;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, int Speed) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        sounds = new SoundPlayer(context);
        c = context;
        intent = new Intent(c, result.class)/*.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/;
        holder = getHolder();
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;
        currentScore = 0;
        //

        BF = MediaPlayer.create(context, R.raw.duckflapping);
        DB = MediaPlayer.create(context, R.raw.dogbark);
        BD = MediaPlayer.create(context, R.raw.birddead);
        DCEL = MediaPlayer.create(context, R.raw.dogcel);
        DLGH = MediaPlayer.create(context, R.raw.doglgh);
        DW = MediaPlayer.create(context, R.raw.dogwalkin);



        Rect dest = new Rect(0, 0, getWidth(), getHeight());

        bmpBG = BitmapFactory.decodeResource(getResources(), R.drawable.modern_background);
        background = Bitmap.createScaledBitmap(bmpBG, ScreenWidth, ScreenHeight, false);
        bird = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.stain);
        dog = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        score = BitmapFactory.decodeResource(getResources(), R.drawable.score);
        birdsGUI = BitmapFactory.decodeResource(getResources(), R.drawable.shots);
        shots = BitmapFactory.decodeResource(getResources(), R.drawable.score);
        birdIcon = BitmapFactory.decodeResource(getResources(), R.drawable.birdicon);
        birdDead = BitmapFactory.decodeResource(getResources(), R.drawable.birdhit);
        bulletIcon = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);


        //set current game state
        this.gameState = GameState.DOGWALKIN;

        shotsLeft = 3;




        timer.scheduleAtFixedRate(task, 1000, 1000);

        //



        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });



   }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Sprite createSprite(int resource, String name, int speed, boolean isAnimated){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        // and then declare object of Sprite class
        return new Sprite(this, bmp, name, speed, isAnimated);
    }

    @Override
    protected void onDraw(Canvas canvas) {

            if (canvas != null) {
                canvas.drawBitmap(background, 0, 0, null);


                if(this.gameState == gameState.DOGWALKIN){
                    if(!spriteLoaded) {
                        DW.start();

                        dogSprite = new Sprite(this, dog, true);
                        spriteScore = new Sprite(this, score);
                        spriteShots = new Sprite(this, shots);
                        spriteBirdsGUI = new Sprite(this, birdsGUI);
                        spriteLoaded = true;
                    }
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(100);
                    canvas.drawText("LEVEL: " + currentLevel, canvas.getWidth() /2 - canvas.getWidth()/12, canvas.getHeight() /2, paint);
                    dogSprite.onDraw(canvas);
                }

                if(this.gameState == gameState.DOGJUMP){
                    DW.stop();
                    dogSprite.onDraw(canvas);
                    DB.start();
                }
                if (this.gameState == gameState.BIRDFLYING) {
                    BF.start();
                    BF.setLooping(true);
                    if (!birdSpriteLoaded) {
                        //setup sprite
                        birdSprite = new Sprite(this, bird, true);
                        spriteScore = new Sprite(this, score);
                        spriteShots = new Sprite(this, shots);
                        spriteBirdsGUI = new Sprite(this, birdsGUI);
                        //spriteBullet = new Sprite(this, bulletIcon);
                        birdSpriteLoaded = true;
                    }
                    Paint paint = new Paint();

                    birdSprite.onDraw(canvas);
                    spriteScore.drawCanvas(canvas, 0, currentBird);
                    spriteBirdsGUI.drawCanvas(canvas, 1, currentBird);
                    spriteShots.drawCanvas(canvas, 2, currentBird);

                }

                if(this.gameState == gameState.BIRDDEAD){
                    BD.start();
                    BD.setLooping(false);
                    birdSprite.drawCanvas(canvas, 3, currentBird);
                    BD.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){@Override public void onCompletion(MediaPlayer mp) {
                            birdSpriteLoaded = false;
                            gameState = gameState.DOGCELEBRATING;
                            shotsLeft = 3;


                    }});
                }

                if(this.gameState == gameState.DOGCELEBRATING){
                    DCEL.start();
                    DCEL.setLooping(false);
                    spriteShots.drawCanvas(canvas, 4, currentBird);
                    DCEL.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){@Override public void onCompletion(MediaPlayer mp) {
                        if(currentBird < 10){
                            birdSpriteLoaded = false;
                            gameState = gameState.BIRDFLYING;
                            shotsLeft = 3;

                        }
                        else{
                            for(int i = 0; i < ShotRecord.length; i++){
                                ShotRecord[i] = true;
                            }
                            currentBird = 0;
                            spriteLoaded = false;
                            currentLevel++;
                            if(currentLevel < 11) {
                                gameState = gameState.DOGWALKIN;
                            }
                        }


                    }});
                }

                if(this.gameState == gameState.DOGLAUGH){
                    DLGH.start();
                    DLGH.setLooping(false);
                    spriteShots.drawCanvas(canvas, 5, currentBird);
                    DLGH.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){@Override public void onCompletion(MediaPlayer mp) {
                        if(currentBird < 10){
                            birdSpriteLoaded = false;
                            gameState = gameState.BIRDFLYING;
                            shotsLeft = 3;

                        }
                        else{
                            for(int i = 0; i < ShotRecord.length; i++){
                                ShotRecord[i] = true;
                            }
                            currentBird = 0;
                            spriteLoaded = false;
                            currentLevel++;
                            if(currentLevel < 11) {
                                gameState = gameState.DOGWALKIN;
                            }
                        }


                    }});
                }
            }
            displayStats(canvas);





    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.gameState == gameState.BIRDFLYING) {
            if (shotsLeft > 0) {
                sounds.playGunShot();
            }
            shotsLeft--;
            synchronized (getHolder()) {

                if (birdSprite.isCollision(event.getX(), event.getY()) && birdSprite.visibility && shotsLeft >= 0) {

                    currentScore += 1000;

                        if (currentLevel > 10) {
                            intent.putExtra("SCORE", currentScore);
                            this.gameState = GameState.GAMEOVER;
                            c.startActivity(intent);
                        }

                        spriteLoaded = false;
                    birdSprite.visibility = false;

                    this.gameState = GameState.BIRDDEAD;
                    BF.stop();
                    birdsKilled++;
                    currentBird++;
                }

                if(birdSprite.visibility == true && shotsLeft == 0){
                    birdSprite.visibility = false;
                    gameState = GameState.DOGLAUGH;
                    ShotRecord[currentBird] = false;
                    currentBird++;
                }
                return super.onTouchEvent(event); // has to be returned by this method
            }

        }

        return false;
    }





    static int numlength(int n)
    {
        if (n == 0) return 1;
        int l;
        n=Math.abs(n);
        for (l=0;n>0;++l)
            n/=10;
        return l;
    }


    public void displayStats(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        int tempScore = numlength(currentScore);

        String leadingZeros = "";
        while(tempScore < 6) {
            leadingZeros += "0";
            tempScore++;
        }
        canvas.drawText(leadingZeros + Integer.toString(currentScore), canvas.getWidth() - canvas.getWidth()/6, canvas.getHeight() - canvas.getHeight()/9, paint);
    }




}


//synchronize


