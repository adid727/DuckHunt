package com.project.AdidNissan.finalproject;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Sprite {

       public int x; // sprite coordinate x
       public int y; // sprite coordinate y
       private int xSpeed; // sprite x speed
       private int ySpeed; // sprite y speed
       public int width;
       public int height;
       public String name;

       private GameView gameView;  // reference to GameView

       public int ScreenWidth;
       public int ScreenHeight;
       public Bitmap bmp;         // sprite Bitmap
       Random mRandom = new Random();

    int currentFrame = 0;
    int currentDogJumpFrame = 0;
    int MaxFrame = 2;
    int MaxDogFrame = 4;
    int direction = -1;

    int initSpeedX = 5;
    int initSpeedY = -5;
    boolean visibility = false;

    //keep track of time
    long last_time = System.nanoTime();

    long birdDirectionTime = System.nanoTime();

    long birdFallingTime = System.nanoTime();

    long birdIconFlashing = System.nanoTime();

     
       @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
       public Sprite(GameView gameView, Bitmap bmp, String Name, int speed, boolean isAnimated) {
             this.name = Name;
             this.gameView=gameView;
             this.bmp=bmp;
             this.width = bmp.getWidth();
           //this.width = 80;
           //this.height = 80;
             this.height = bmp.getHeight();
             this.x = ThreadLocalRandom.current().nextInt(10, 500 + 1);
             this.y = ThreadLocalRandom.current().nextInt(10, 500 + 1);
             this.xSpeed = ThreadLocalRandom.current().nextInt(-10, speed + 1);
             this.ySpeed= ThreadLocalRandom.current().nextInt(10, speed + 1);
       }

    public Sprite(GameView GM, Bitmap bitmap, boolean visibile){
        gameView = GM;
        visibility = visibile;
        ScreenWidth = GM.ScreenWidth;
        ScreenHeight = GM.ScreenHeight;
        bmp = bitmap;
        if(gameView.gameState == GameView.GameState.DOGWALKIN){
            width = bitmap.getWidth() / 6 - 21;
            height = bitmap.getHeight() / 2;
            x =  -200;
            y = 850;
            this.xSpeed = 5;
            this.ySpeed = 0;


        }
        if(gameView.gameState == GameView.GameState.BIRDFLYING) {
            width = bitmap.getWidth() / 3;
            height = bitmap.getHeight() / 4;
            x = getRandomStartingPoint();
            y = 650;
            this.xSpeed = initSpeedX;
            this.ySpeed = initSpeedY;
            if(this.ySpeed > 0){
                this.ySpeed *=-1;
            }
            else if(this.ySpeed == 0){
                this.ySpeed = -3;
            }
            Matrix flipHorizontalMatrix = new Matrix();
            if (this.xSpeed < 0) {
                flipHorizontalMatrix.setScale(-1, 1);
                flipHorizontalMatrix.postTranslate(bmp.getWidth(), 0);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), flipHorizontalMatrix, true);
            }
        }
    }

    //gui constructor
    public Sprite(GameView GM, Bitmap bitmap){
        gameView = GM;
        visibility = true;
        ScreenWidth = GM.ScreenWidth;
        ScreenHeight = GM.ScreenHeight;
        bmp = bitmap;
        if(gameView.gameState == GameView.GameState.DOGWALKIN){
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            x = 0;
            y = 800;
            this.xSpeed = 0;
            this.ySpeed = 0;

        }
        if(gameView.gameState == GameView.GameState.BIRDFLYING) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            x = y = 400;
            this.xSpeed = 0;
            this.ySpeed = 0;
        }
        if(gameView.gameState == GameView.GameState.DOGCELEBRATING){
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            x = y = 400;
            this.xSpeed = 0;
            this.ySpeed = 5;
        }
    }

    // boundaries collision for a single bitmap
       private void update(Sprite sprite) {
           // boundaries collision for east / west
           if (sprite.x > gameView.getWidth() - bmp.getWidth() - sprite.xSpeed) {
               sprite.xSpeed = -sprite.xSpeed;
             }
             if (sprite.x + sprite.xSpeed< 0) {
                 sprite.xSpeed = -sprite.xSpeed;
             }
           sprite.x = sprite.x + sprite.xSpeed;

           // boundaries collision for north /south
           if (sprite.y > sprite.gameView.getHeight() - bmp.getHeight() - sprite.ySpeed) {
               sprite.ySpeed = -sprite.ySpeed;
           }
           if (sprite.y + sprite.ySpeed< 0) {
               sprite.ySpeed = -sprite.ySpeed;
           }
           sprite.y = sprite.y + sprite.ySpeed;
       }

       public void onDraw(Canvas canvas, Sprite sprite) {
               update(sprite);
               //
               canvas.drawBitmap(bmp, x , y, null);
       }

    public boolean isCollision(float x2, float y2){
        //todo auto-generated method stub
        //
        if(x2 > x - width && x2 < x + width && y2 > y - height && y2 < y + height){
            return true;
        }
        return false;
    }

    public void onDraw(Canvas c){
        if(visibility) {
            long time = System.nanoTime();
            update();

            int delta_time = (int) ((time - last_time) / 1000000);
            int delta_birdTime = (int) ((time - birdDirectionTime) / 1000000);
            Rect src = new Rect();
            Rect dst = new Rect();

            if (gameView.gameState == GameView.GameState.DOGWALKIN) {

                src = new Rect(0 + (currentFrame * width), 0, width + (currentFrame * width), height);
                //how scalled is the sprite
                dst = new Rect(x, y, x + width, y + height);
                if (delta_time > 50) {
                    if (MaxDogFrame == 4) {
                        currentFrame++;
                        if (currentFrame == 4) {
                            MaxDogFrame = 0;
                        }
                    } else if (MaxDogFrame == 0) {
                        currentFrame--;
                        if (currentFrame == 0) {
                            MaxDogFrame = 4;
                        }
                    }
                    last_time = time;
                }
            }
            if (gameView.gameState == GameView.GameState.DOGJUMP) {
                src = new Rect(0 + (currentDogJumpFrame * width), height, width + (currentDogJumpFrame * width), height * 2);
                //how scalled is the sprite
                dst = new Rect(x, y, x + width, y + height);
                if (delta_time > 1000 && currentDogJumpFrame == 2) {
                    gameView.gameState = GameView.GameState.BIRDFLYING;
                    gameView.birdSprite = new Sprite(gameView, gameView.bird, true);
                    gameView.birdSpriteLoaded = true;
                    last_time = time;
                }
                if (delta_time > 1000 && currentDogJumpFrame < 2) {
                    currentDogJumpFrame++;
                    last_time = time;
                }
            } else if (gameView.gameState == GameView.GameState.BIRDFLYING) {

                if (delta_birdTime > 3000) {

                    changeDirection();
                    //update();
                    birdDirectionTime = time;
                }
                //checkBondaries();
                //direction = 2;
                //right
                if (direction == 0) {
                    src = new Rect(0 + (currentFrame * width), 0, width + (currentFrame * width), height);
                    //how scalled is the sprite
                    dst = new Rect(x, y, x + width, y + height);
                }
                //left
                if (direction == 1) {

                    //The cutoff sprite
                    src = new Rect(width * 2 - (currentFrame * width), 0, width * 3 - (currentFrame * width), height);
                    //how scalled is the sprite
                    dst = new Rect(x, y, x + width, y + height);

                }
                //topLeft
                else if (direction == 2) {
                    src = new Rect(0 + (currentFrame * width), height, width + (currentFrame * width), height * 2);
                    //how scalled is the sprite
                    dst = new Rect(x, y, x + width, y + height);
                } else if (direction == 3) {
                    //The cutoff sprite
                    src = new Rect(width * 2 - (currentFrame * width), height, width * 3 - (currentFrame * width), height * 2);
                    //how scalled is the sprite
                    dst = new Rect(x, y, x + width, y + height);
                } else if (direction == 4) {
                    src = new Rect(0 + (currentFrame * width), height * 2, width + (currentFrame * width), height * 3);
                    //how scalled is the sprite
                    dst = new Rect(x, y, x + width, y + height);
                }
                //change direction

                //how fast does the animation run (once every second)
                if (delta_time > 50) {
                    if (MaxFrame == 2) {
                        currentFrame++;
                        if (currentFrame == 2) {
                            MaxFrame = 0;
                        }
                    } else if (MaxFrame == 0) {
                        currentFrame--;
                        if (currentFrame == 0) {
                            MaxFrame = 2;
                        }
                    }

                    last_time = time;
                }


            }
            c.drawBitmap(bmp, src, dst, null);
        }
    }

    public void resultingCollision(){
        this.xSpeed *= -1;
        this.ySpeed *= -1;
    }

    private void update(){
        if(gameView.gameState == GameView.GameState.DOGWALKIN){
            x += xSpeed;
            y += ySpeed;
            if(x == 800){

                gameView.gameState = GameView.GameState.DOGJUMP;
                //currentFrame = 0;
            }
        }
        else if(gameView.gameState == GameView.GameState.DOGJUMP){
            if(currentDogJumpFrame > 0 && currentDogJumpFrame < 2) {
                x += 2;
                y -= 2;
            }
        }
        else if(gameView.gameState == GameView.GameState.BIRDFLYING) {
            Matrix flipHorizontalMatrix = new Matrix();
            if (x + width > ScreenWidth) {
                xSpeed *= -1;
                flipHorizontalMatrix.setScale(-1, 1);
                flipHorizontalMatrix.postTranslate(bmp.getWidth(), 0);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), flipHorizontalMatrix, true);
            } else if (x < 0) {
                xSpeed *= -1;
                flipHorizontalMatrix.setScale(-1, 1);
                flipHorizontalMatrix.postTranslate(bmp.getWidth(), 0);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), flipHorizontalMatrix, true);
            }
            /*if(y - height > ScreenHeight){
                gameView.gameState = GameView.GameState.DOGLAUGH;
            }*/

            //going right
            if (xSpeed > 0 && ySpeed == 0 && x + width < ScreenWidth && direction != 0) {
                direction = 0;


            }

            //going left
            else if (xSpeed < 0 && ySpeed == 0 && x > 0 && direction != 1) {
                direction = 1;
            }

            //going up and right
            else if (xSpeed > 0 && ySpeed < 0 && direction != 2) {
                direction = 2;
            }

            //going up and left
            else if (xSpeed < 0 && ySpeed < 0 && direction != 3) {
                direction = 3;
            }

            //going up
            else if (ySpeed < 0 && xSpeed == 0&& direction != 4) {
                direction = 4;
            }
            /*else if(){
                direction = 2;
            }*/
            x += xSpeed;
            y += ySpeed;
        }
    }

    public void drawCanvas(Canvas c, int GUI, int currentBird){
        Rect src = new Rect();
        Rect dst = new Rect();

        //score
        if(GUI == 0) {
            x = ScreenWidth - 500;
            y = ScreenHeight - 600;
            src = new Rect(0, 0, width, height);
            double newHeight = height * 1.7;
            //how scalled is the sprite
            dst = new Rect(x, y, x + width, y + ((int) newHeight));
            c.drawBitmap(bmp, src, dst, null);
        }
        //birds gui
        else if(GUI == 1){



            x = 560;
            y = ScreenHeight - 340;
            src = new Rect(0, 0, width, height);
            double newHeight = height /2;
            //how scalled is the sprite
            dst = new Rect(x, y, x + (int)(width * 1.6), y + ((int) newHeight));
            c.drawBitmap(bmp, src, dst, null);
            //draw bird icon
            //create sprites based on the ShotRecord boolean
            for(int i = 0; i < gameView.ShotRecord.length; i++) {
                if(gameView.ShotRecord[i] && i == 0) {
                    gameView.spriteBirdIcon0 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon0);

                } else if(i == 0){
                    gameView.spriteBirdDead0 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead0);
                }
                if(gameView.ShotRecord[i] && i == 1) {
                    gameView.spriteBirdIcon1 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon1);
                } else if(i == 1){
                    gameView.spriteBirdDead1 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead1);
                }
                if(gameView.ShotRecord[i] && i == 2) {
                    gameView.spriteBirdIcon2 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon2);
                } else if(i == 2){
                    gameView.spriteBirdDead2 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead2);
                }
                if(gameView.ShotRecord[i] && i == 3) {
                    gameView.spriteBirdIcon3 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon3);
                } else if(i == 3){
                    gameView.spriteBirdDead3 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead3);
                }
                if(gameView.ShotRecord[i] && i == 4) {
                    gameView.spriteBirdIcon4 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon4);
                } else if(i == 4){
                    gameView.spriteBirdDead4 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead4);
                }
                if(gameView.ShotRecord[i] && i == 5) {
                    gameView.spriteBirdIcon5 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon5);
                } else if(i == 5){
                    gameView.spriteBirdDead5 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead5);
                }
                if(gameView.ShotRecord[i] && i == 6) {
                    gameView.spriteBirdIcon6 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon6);
                } else if(i == 6){
                    gameView.spriteBirdDead6 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead6);
                }
                if(gameView.ShotRecord[i] && i == 7) {
                    gameView.spriteBirdIcon7 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon7);
                } else if(i == 7){
                    gameView.spriteBirdDead7 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead7);
                }
                if(gameView.ShotRecord[i] && i == 8) {
                    gameView.spriteBirdIcon8 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon8);
                } else if(i == 8){
                    gameView.spriteBirdDead8 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead8);
                }
                if(gameView.ShotRecord[i] && i == 9) {
                    gameView.spriteBirdIcon9 = new Sprite(gameView, gameView.birdIcon);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdIcon9);
                } else if(i == 9){
                    gameView.spriteBirdDead9 = new Sprite(gameView, gameView.birdDead);
                    drawIcon(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.ShotRecord[i], gameView.spriteBirdDead9);
                }


            }

        }
        //shots
        else if(GUI == 2){
            x = 20;
            y = ScreenHeight - 600;
            src = new Rect(0, 0, width, height);
            double newHeight = height * 1.7;
            //how scalled is the sprite
            dst = new Rect(x, y, x + width, y + ((int) newHeight));
            c.drawBitmap(bmp, src, dst, null);

            for(int i = 0; i < gameView.shotsLeft; i ++) {
                gameView.spriteBullet = new Sprite(gameView, gameView.bulletIcon);
                    drawIconBullets(c, this.x, this.y, this.ScreenWidth, this.ScreenHeight, i, gameView.spriteBullet);


            }
        }

        else if(GUI == 3){
            long time = System.nanoTime();

            this.visibility = true;
            src = new Rect(width, height * 3, 2 * width, height * 4);
            this.xSpeed = 0;
            this.ySpeed = 5;
            //how scalled is the sprite
            dst = new Rect(x, y, x + width, y + height);
            int delta_time = (int) ((time - birdFallingTime) / 1000000);
            if(delta_time > 50 && this.y < 650) {
                updateBirdFalling(c, gameView.birdSprite);
            }




        }

        else if (GUI == 4){
            this.setVisibility(true);
            long time = System.nanoTime();
            int delta_time = (int) ((time - birdFallingTime) / 1000000);
            if(delta_time > 50) {
                updateDogCelebrating(c, gameView.dogSprite);
            }
        }

        else if (GUI == 5){
            this.setVisibility(true);
            long time = System.nanoTime();
            int delta_time = (int) ((time - birdFallingTime) / 1000000);
            if(delta_time > 50) {
                updateDogLaugh(c, gameView.dogSprite);
            }
        }

        //c.drawBitmap(bmp, src, dst, null);
    }


    public void drawIcon(Canvas c, int X, int Y, int ScreenW, int ScreenH, int currentIcon, boolean isAlive, Sprite sprite){

        Rect src = new Rect();
        Rect dst = new Rect();

        long time = System.nanoTime();

        int delta_time = (int) ((time - birdIconFlashing) / 1000000);
        if(currentIcon != gameView.currentBird){
            sprite.visibility = true;
        }



        //calculate the width of one bird icon space
        int birdIconWidth = width / 10;



        src = new Rect(0, 0, sprite.width, sprite.height);
        double newHeight = height * 0.3;
        //how scalled is the sprite
        dst = new Rect(x + (birdIconWidth) + (int)(birdIconWidth * currentIcon * 1.45), y+ birdIconWidth, x + sprite.width + (birdIconWidth) + (int)(birdIconWidth * currentIcon * 1.45), y + ((int) newHeight)+50);
        if(isAlive) {
            if(currentIcon == gameView.currentBird) {
                if (delta_time > 50) {

                    //c.drawBitmap(gameView.birdIcon, src, dst, null);
                    //update();
                    birdIconFlashing = time;
                    if(sprite.visibility){
                        sprite.setVisibility(false);

                    }
                    else{
                        sprite.setVisibility(true);
                    }
                }
            }
            if(sprite.visibility) {
                c.drawBitmap(gameView.birdIcon, src, dst, null);
            }
        }else{
            c.drawBitmap(gameView.birdDead, src, dst, null);
        }
    }

    public void drawIconBullets(Canvas c, int X, int Y, int ScreenW, int ScreenH, int currentIcon,  Sprite sprite){
        Rect src = new Rect();
        Rect dst = new Rect();

        //calculate the width of one bird icon space
        int bulletIconWidth = width / 3;



        src = new Rect(0, 0, sprite.width, sprite.height);
        double newHeight = height * 0.3;
        //how scalled is the sprite
        dst = new Rect(x + (bulletIconWidth/4) + (currentIcon * bulletIconWidth), y+ (bulletIconWidth *2), x + sprite.width + (int)(bulletIconWidth/4)+ (currentIcon * bulletIconWidth), y +  sprite.height + (int)(bulletIconWidth *2.5));
        c.drawBitmap(gameView.bulletIcon, src, dst, null);
    }
    public String getName(){
        return name;
    }

    public void setVisibility(boolean a){
        visibility = a;
    }

    public int getRandomStartingPoint(){
        int i = mRandom.nextInt(this.ScreenWidth);
        if (i > 250){
            i-=200;
        }
        return i;
    }

    public int getNewDirection(){
        return mRandom.nextInt(4);
    }
    //change bird direction
    public void changeDirection(){
        direction = getNewDirection();
        //going right
        if(direction == 0){
            this.ySpeed = 0;
        }
        //going left
        else if(direction == 1){
            this.ySpeed = 0;
        }

        //going up and right
        else if(direction == 2){
                this.ySpeed = initSpeedY;
                this.xSpeed = initSpeedX;
        }

        //going up and left
        else if(direction == 3){
            this.ySpeed = initSpeedY;
            this.xSpeed = -initSpeedX;
        }

        //going up and left
        else if(direction == 4){
            this.ySpeed = -initSpeedY;
            this.xSpeed = 0;
        }
    }

    public void checkBondaries(){
        if(this.x + this.width + this.xSpeed >= ScreenWidth){
            this.xSpeed *= -1;
        }
        else if(this.x - this.xSpeed <= 0 ){
            this.xSpeed = -1;
        }
        if(this.y + this.height + this.ySpeed >= ScreenHeight){
            this.ySpeed *= -1;
        }
        /*else if(this.y - this.ySpeed <= 0){
            this.ySpeed *= -1;
        }*/

        //going right
        if (xSpeed > 0 && ySpeed == 0 && x + width < ScreenWidth) {
            direction = 0;


        }

        //going left
        else if (xSpeed < 0 && ySpeed == 0 && x > 0) {
            direction = 1;
        }

        //going up and right
        else if (xSpeed > 0 && ySpeed > 0 /*((java.lang.Math.abs(xSpeed) - java.lang.Math.abs(ySpeed)) < 1) */&& direction != 2) {
            direction = 2;
        }

        //going up and left
        else if (xSpeed < 0 && ySpeed < 0 /*((java.lang.Math.abs(xSpeed) - java.lang.Math.abs(ySpeed)) < 1)*/ && direction != 3) {
            direction = 3;
        }

        //going up
        else if (ySpeed > java.lang.Math.abs(xSpeed) && ySpeed < 0 && direction != 4) {
            direction = 4;
        }
    }

    public void updateBirdFalling(Canvas c, Sprite birdSprite){

        Rect src = new Rect(width, height * 3, 2 * width, height * 4);

        //how scalled is the sprite
        Rect dst = new Rect(x, y, x + width, y + height);
            this.y += this.ySpeed;
            c.drawBitmap(bmp, src, dst, null);
    }

    public void updateDogCelebrating(Canvas c, Sprite dogSprite){
        bmp = gameView.dog;
        this.x = gameView.birdSprite.x- 150;
        this.y = 650;
        this.setVisibility(true);
        Rect src = new Rect(5* width, 0, 6 * width + 100, height);
        //how scalled is the sprite
        Rect dst = new Rect(x, y, x + width, y + height);

        c.drawBitmap(bmp, src, dst, null);
    }

    public void updateDogLaugh(Canvas c, Sprite dogSprite){
        bmp = gameView.dog;
        this.x = gameView.birdSprite.x- 150;
        this.y = 750;
        this.setVisibility(true);
        Rect src = new Rect(3* width, height, 4 * width + 100, 2 * height);
        //how scalled is the sprite
        Rect dst = new Rect(x, y, x + width, y + height);

        c.drawBitmap(bmp, src, dst, null);
    }
}   
