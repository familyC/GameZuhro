package id.haqiqi_studio.doraemon_dorayaki;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by HarID on 2018/25/05.
 */

public class GameView extends View {

    MathUtils math = new MathUtils();

    // Canvas
    private int canvasWidth;
    private int canvasHeight;
    private int levelPos = 1;
    int decreator = 20;

    MediaPlayer hitBlueBall;
    MediaPlayer hitBlackBall;
    MediaPlayer hitSnow;
    MediaPlayer getAward;

    Random random = new Random();
    // Bird
    //private Bitmap zuhro;
    private Bitmap zuhro[] = new Bitmap[2];
    private int zuhroX = 10;
    private int zuhroY;
    private int zuhroSpeed;

    // Blue Ball
    private int blueX;
    private int blueY;
    private int blueSpeed = 12;
    private Paint bluePaint = new Paint();

    // Black Ball
    private int blackX;
    private int blackY;
    private int blackSpeed = 20;
    private Bitmap blackPaint[] = new Bitmap[1];

    // Snow
    private int dorayakiBeracunX;
    private int dorayakiBeracunY;
    private int dorayakiBeracunSpeed = 7;
    private Bitmap dorayakiBeracun[] = new Bitmap[1];


    // Soulmate
    private int ucilX;
    private int ucilY;
    private int ucilSpeed = 2;
    private Bitmap ucil;

    // Background
    private Bitmap bgImage[] = new Bitmap[3];

    // Card
    private Bitmap card[] = new Bitmap[3];

    //Ganjil
    private int dorayakiX;
    private int dorayakiY;
    private int dorayakiSpeed = 8;
    private Bitmap dorayaki[] = new Bitmap[3];

    //enap
    private int genapX;
    private int genapY;
    private int genapSpeed = 8;
    private Bitmap genap[] = new Bitmap[3];

    // Score
    private Paint scorePaint = new Paint();
    private int score;

    // Level
    private Paint levelPaint = new Paint();

    // Game State
    private Paint state = new Paint();

    // Life
    private Bitmap life[] = new Bitmap[2];
    private int life_count;

    // Status Check
    private boolean touch_flg = false;
    private boolean is_slow = false;

    private Rect rect;

    private int dWidth, dHeight;

    Boolean is_show = true;

    //region GameView
    public GameView(Context context) {
        super(context);

        bgImage[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        bgImage[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bg1);
        bgImage[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

        ucil = BitmapFactory.decodeResource(getResources(), R.drawable.superucil);
        dorayaki[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dorayaki);


        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rect = new Rect(0, 0, dWidth, dHeight);

        zuhro[0] = BitmapFactory.decodeResource(getResources(), R.drawable.zuhyo);
        zuhro[1] = BitmapFactory.decodeResource(getResources(), R.drawable.zuhro);


        dorayakiBeracun[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dorayaki_beracun);

        bluePaint.setColor(Color.BLUE);
        bluePaint.setAntiAlias(false);

//        blackPaint.setColor(Color.BLACK);
//        blackPaint.setAntiAlias(false);

        blackPaint[0] = BitmapFactory.decodeResource(getResources(), R.drawable.peluru);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(32);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        state.setColor(Color.BLACK);
        state.setTextSize(32);
        state.setTypeface(Typeface.DEFAULT_BOLD);
        state.setAntiAlias(true);

        levelPaint.setColor(Color.DKGRAY);
        levelPaint.setTextSize(32);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        levelPaint.setTextAlign(Paint.Align.CENTER);
        levelPaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_g);

        hitBlackBall = MediaPlayer.create(context, R.raw.brukk);
        hitBlueBall = MediaPlayer.create(context, R.raw.point);
        hitSnow = MediaPlayer.create(context, R.raw.hit_snow);
        getAward = MediaPlayer.create(context, R.raw.award);

        // First position.
        zuhroY = 500;
        score = 0;
        life_count = 10;
    }
    //endregion

    @Override
    protected void onDraw(Canvas canvas) {

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        changeState(canvas, rect);

        //region Doraemon Speed
        int minBirdY = zuhro[0].getHeight() - 30;
        int maxBirdY = canvasHeight - zuhro[0].getHeight();

        zuhroY += zuhroSpeed;
        if (zuhroY < minBirdY) zuhroY = minBirdY;
        if (zuhroY > maxBirdY) zuhroY = maxBirdY;
        zuhroSpeed += 2;
        //endregion

        //region Touch Flag
        if (touch_flg) {
            // Flap wings.
            canvas.drawBitmap(zuhro[0], zuhroX, zuhroY, null);
            touch_flg = false;
        } else {
            canvas.drawBitmap(zuhro[1], zuhroX, zuhroY, null);
        }
        //endregion

        //region Dorayaki Beracun
        if (hitCheck(dorayakiBeracunX, dorayakiBeracunY)) {
            dorayakiBeracunX = -100;
            hitSnow.start();

            score -= decreator;
        }


        //region Snow
        if (!is_slow) {
            showDorayakiBeracun(canvas, dorayakiBeracunX, dorayakiBeracunY);
        }

        if (hitCheck(dorayakiBeracunX, dorayakiBeracunY)) {
            is_slow = true;
            dorayakiBeracunX = -100;
            hitSnow.start();
            score -= 20;

        }
        if (score < 0) {
            gameOver(canvas);
        }

        if (score % 110 == 0) {
            is_slow = false;
            blackSpeed = 20;
            blueSpeed = 12;
        }

        dorayakiBeracunX -= dorayakiBeracunSpeed;
        if (dorayakiBeracunX < 0) {
            dorayakiBeracunX = canvasWidth + 200;
            dorayakiBeracunY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }

        showDorayakiBeracun(canvas, dorayakiBeracunX, dorayakiBeracunY);
        //endregion

        if (score % 110 == 0) {
            is_slow = false;
            blackSpeed = 20;
            blueSpeed = 12;
        }
        //endregion

        //region Dorayaki

        dorayakiX -= dorayakiSpeed;
        if (hitCheck(dorayakiX, dorayakiY)) {
            hitBlueBall.start();
            is_slow = false;
            score += 10;
            checkPoint();
            dorayakiX = -100;
        }


        if (dorayakiX < 0) {
            dorayakiX = canvasWidth + 20;
            dorayakiY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }
        canvas.drawBitmap(dorayaki[0], dorayakiX, dorayakiY, null);
        //endregion

        //region Black Ball
        genapX -= genapSpeed;
        if (hitCheck(genapX, genapY)) {
            hitBlackBall.start();
            score += 10;
            checkPoint();
            genapX = -100;
        }

        if (genapX < 0) {
            genapX = canvasWidth + 200;
            genapY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }
        canvas.drawBitmap(dorayaki[0], genapX, genapY, null);
        //endregion


        checkLife(canvas, life_count);
        // Score
        canvas.drawText("Score :" + score, 15, 60, scorePaint);


        if (if_over()) {
            gameOver(canvas);
        }
    }

    void gameOver(Canvas canvas) {
        canvas.drawText("Game Over", canvasWidth / 2 - 30, canvasHeight / 2, state);
        zuhroSpeed = 0;
        blackSpeed = 0;
        blueSpeed = 0;
        dorayakiSpeed = 0;
        zuhroY = -100;
    }

    void showDorayakiBeracun(Canvas canvas, int x, int y) {

        if (score % 20 == 0 || !is_slow) {
            is_show = true;
            if (is_show) {
                is_show = false;
                canvas.drawBitmap(dorayakiBeracun[0], x, y, null);
            }

        }
    }

    void checkPoint() {
        if (score % 100 == 0) {
            getAward.start();
        }
    }

    //region Check Life
    void checkLife(Canvas canvas, int _life) {
        if (_life == 3) {
            // Game Over
            //Log.v("Message", "Game Over");
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);
        } else if (_life == 2) {
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);
        } else if (_life == 1) {
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);
        } else {
            //canvas.drawText("Game Over", canvasWidth / 2 - 40, canvasHeight / 2, state);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);


        }
    }

    boolean if_over() {
        if (life_count <= 0) {
            return true;
        } else {
            return false;
        }
    }
    //endregion

    void showUcil(Canvas canvas, int x, int y) {
        if (score % 310 == 0 && score != 0) {
            canvas.drawBitmap(ucil, x, y, null);
        }
    }


    //region Change State
    void changeState(Canvas canvas, Rect rect) {
        if (score >= 100 && score <= 200) {
            canvas.drawBitmap(bgImage[1], null, rect, null);
            canvas.drawText("Level 2", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 10;
            genapSpeed = 10;
            dorayakiBeracunSpeed = 8;
            decreator = 30;
            levelPos = 1; }
        else if (score >= 200 && score <= 300) {
            canvas.drawBitmap(bgImage[1], null, rect, null);
            canvas.drawText("Level 2", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 13;
            genapSpeed = 13;
            dorayakiBeracunSpeed = 11;
            decreator = 50;
            levelPos = 2;
        } else if (score > 300 && score <= 400) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 3", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 16;
            genapSpeed = 16;
            dorayakiBeracunSpeed = 14;
            decreator = 80;
            levelPos = 3;
        } else if (score > 400 && score <= 500) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 4", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 19;
            genapSpeed = 19;
            dorayakiBeracunSpeed = 17;
            decreator = 120;
            levelPos = 4;
        } else if (score > 500 && score <= 600) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 5", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 22;
            genapSpeed = 22;
            dorayakiBeracunSpeed = 20;
            decreator = 150;
            levelPos = 5;
        } else if (score > 600 && score <= 700) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 6", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 25;
            genapSpeed = 25;
            dorayakiBeracunSpeed = 22;
            decreator = 200;
            levelPos = 6;
        }  else if (score > 700 && score <= 800) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 7", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 28;
            genapSpeed = 28;
            dorayakiBeracunSpeed = 25;
            decreator = 200;
            levelPos = 7;
        }  else if (score > 800 && score <= 900) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 8", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 31;
            genapSpeed = 31;
            dorayakiBeracunSpeed = 28;
            decreator = 600;
            levelPos = 8;
        } else if (score > 900 && score <= 1000) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 9", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 34;
            genapSpeed = 34;
            dorayakiBeracunSpeed = 31;
            decreator = 800;
            levelPos = 9;
        } else if (score > 1000) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Level 10", canvasWidth / 2, 60, levelPaint);
            dorayakiSpeed = 37;
            genapSpeed = 37;
            dorayakiBeracunSpeed = 34;
            decreator = 900;
            levelPos = 10;
        }
    }
    //endregion

    void setDifficult(int level, Canvas canvas) {
        if (level == 2) {
            canvas.drawBitmap(dorayaki[0], dorayakiX, dorayakiY, null);
        } else if (level == 3) {
            canvas.drawBitmap(genap[0], genapX, genapY, null);
        } else if (level == 4) {
            canvas.drawBitmap(genap[1], genapX, genapY, null);
        }
    }

    //region Hit Check
    public boolean hitCheck(int x, int y) {
        if (zuhroX < x && x < (zuhroX + zuhro[0].getWidth()) &&
                zuhroY < y && y < (zuhroY + zuhro[0].getHeight())) {
            return true;
        }
        return false;
    }
    //endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch_flg = true;
            zuhroSpeed = -20;
        }
        if (score < 0) {

        }
        return true;
    }
}
