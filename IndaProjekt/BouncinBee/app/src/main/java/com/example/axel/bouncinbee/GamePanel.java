package com.example.axel.bouncinbee;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by axel on 2017-04-06.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;

    private Bee bee;

    private Honey honey;

    private List<Flower> flowers;
    private List<Enemy> enemies;

    private Paint scorePaint = new Paint();
    public int currentScore;

    private Paint paint;
    float min = getScreenHeight() * 2/3;
    float max = getScreenHeight() - 450;


    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);

        bee = new Bee(context, new PointF(300, 300)); // adjust to fit screen better?

        currentScore = 0;

        enemies = new ArrayList<>();
        // Set first Enemy not to come too early in the game
        enemies.add(new Enemy(context, bee, new PointF(500, (float) (Math.random() * getScreenHeight()))));
        // Set maximum amount of enemies on the display at the same time to 4, including Enemy just created
        for (int i = 1; i < 3; i++) {
            float x_position = getScreenHeight() + (float) (Math.random() * 1000);
            float y_position = (float) (Math.random() * getScreenHeight());
            enemies.add(new Enemy(context, bee, new PointF(enemies.get(i - 1).getX_position() + x_position, y_position)));
        }

        honey = new Honey(context, new PointF(getScreenWidth(), 500));

        // Create an fill an ArrayList of the flowers to be drawn
        flowers = new ArrayList<>();
        // Set first flower underneath the Bee
        flowers.add(new Flower(context, new PointF(bee.getX_position(), getScreenHeight() * 2/3)));
        // Randomize the rest
        for (int i = 1; i < 6; i++) {
            // Generate random initial y-coordinate for each flower
            float y_position = min + (float) (Math.random() * (max - min) + 1);
            // x_position marks the coordinate of the right hand side of the previous flower bitmap
            float x_position = flowers.get(i - 1).getX_position() + flowers.get(i - 1).getWidth();
            // Add a random distance to x_position to get final x-coordinate
            x_position += 200 + (float) Math.random() * 450;
            flowers.add(new Flower(context, new PointF(x_position, y_position)));
        }

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(80);

        setFocusable(true);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                for (Enemy enemy: enemies) {
                    if (event.getX() > enemy.getX_position()
                            && event.getX() < enemy.getX_position() + enemy.getResizedBitmap().getWidth()
                            && event.getY() > enemy.getY_position()
                            && event.getY() < enemy.getY_position() + enemy.getResizedBitmap().getHeight()) {

                        enemy.defeat();
                        break;
                    }
                    bee.boostSpeed();
                }
        }
        return true;
    }

    public void update(){
        for(Flower flower: flowers) {
            flower.update();
            //bounce and give score
            if (flower.canBounce(bee.getX_position(), bee.getY_position(), bee.getWidth(), bee.getHeight())) {
                bee.bounce();
                currentScore += 10;
            }
            // Whenever a flower reaches beyond the left edge of the display, reset the flower a random distance outside the right edge of the display
            if (flower.getX_position() + flower.getWidth() <= 0){
                float randomDistance = 200 + (float) Math.random() * 450;
                flower.resetFlower(getScreenWidth() + randomDistance);
            }
        }

        honey.update();
        if (collide(bee, honey)) {
            honey.consume();
            currentScore += 50;
        }
        // Recreate honey when disappeared from screen or consumed
        if (honey.getX_position() + honey.getResizedBitmap().getWidth() < 0 || honey.isConsumed()) {
            honey.unConsume();
            honey.randomizeY_position();
            honey.setX_position(getScreenWidth() + (float) (Math.random() * 500));
        }

        bee.update();
        // If bee reaches beyond the bottom edge of screen, game is over
        if (bee.getY_position() >= getScreenHeight()) {
            gameOver();
        }

        for (Enemy enemy: enemies) {
            if (enemy.hasFled() || enemy.isDefeated()) {
                enemy.resetEnemy();
            }
            if (collide(bee, enemy) && !enemy.hasStolenHoney) {
                enemy.stealHoney();
                currentScore -= 10;
            }
            enemy.update();
        }
    }

    public boolean collide(GameObject object1, GameObject object2) {
        /*      1.x < 2.x + 2.bredd
            &&  1.x + 1.bredd > 2.x
            &&  1.y < 2.y + 2.höjd
            &&  1.y + 1.höjd > 2.y
         */
        return (object1.getX_position() < object2.getX_position() + object2.getResizedBitmap().getWidth()
                && object1.getX_position() + object1.getResizedBitmap().getWidth() > object2.getX_position()
                && object1.getY_position() < object2.getY_position() + object2.getResizedBitmap().getHeight()
                && object1.getY_position() +  object1.getResizedBitmap().getHeight() > object2.getY_position());

        // TODO: better collision algorithm?
        //if (sourceBitmap1. getPixel(x, y) == Color.TRANSPARENT)
    }

    public void gameOver() {
        thread.setRunning(false);
        Intent intent = new Intent(this.getContext(), GameOver.class);
        intent.putExtra("SCORE", currentScore);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getContext().startActivity(intent);

        //surfaceDestroyed(getHolder());
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawColor(Color.BLUE);

        for(Flower flower: flowers) {
            canvas.drawBitmap(flower.getResizedBitmap(), flower.getX_position(), flower.getY_position(), paint);
        }

        if (!honey.isConsumed()) {
            canvas.drawBitmap(honey.getResizedBitmap(), honey.getX_position(), honey.getY_position(), paint);
        }

        canvas.drawBitmap(bee.getResizedBitmap(), bee.getX_position(), bee.getY_position(), paint);

        for (Enemy enemy: enemies) {
            if (!enemy.isDefeated()) {
                canvas.drawBitmap(enemy.getResizedBitmap(), enemy.getX_position(), enemy.getY_position(), paint);
            }
        }

        canvas.drawText("Score: "+ currentScore, 100, 200, scorePaint);
    }
}