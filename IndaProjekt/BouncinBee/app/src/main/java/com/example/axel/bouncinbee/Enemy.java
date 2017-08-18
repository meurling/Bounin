package com.example.axel.bouncinbee;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import java.util.Vector;

/**
 * Created by axel on 2017-04-06.
 */

public class Enemy implements GameObject {

    private Bitmap bitmap;
    private Bitmap resizedBitmap;
    private Paint paint = new Paint();

    private Bee bee;
    private PointF position;

    private boolean defeated;

    //get the individual phone size
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    float screenWidth = metrics.widthPixels;
    float screenHeight = metrics.heightPixels;

    boolean hasStolenHoney;


    public Enemy (Context context, Bee bee, PointF position) {

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.angrybee);
        this.resizedBitmap = getResizedBitmap(bitmap, 250, 250);

        this.bee = bee;
        this.position = position;

        defeated = false;
        hasStolenHoney = false;
    }

    public float getX_position(){
        return position.x;
    }
    public float getY_position(){
        return position.y;
    }

    public void randomizeY_position() {
        position.y = (float) Math.random() * screenHeight;
    }

    public void stealHoney() {
        hasStolenHoney = true;
    }

    // If the Enemy has stolen Honey and escaped the screen hasFled returns true
    public boolean hasFled() {
        return hasStolenHoney && (getX_position() + resizedBitmap.getWidth()) <= 0;
    }

    public void resetEnemy() {
        hasStolenHoney = false;
        defeated = false;
        position.x = screenWidth + (float) Math.random() * 5000;
        randomizeY_position();
    }

    public void defeat() {
        defeated = true;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public Bitmap getResizedBitmap(){
        return resizedBitmap;
    }
    //Resize the Bee image to fit better on screen
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(resizedBitmap, position.x, position.y, paint);
    }

    @Override
    public void update(Point point) {
        position.x = point.x;
        position.y = point.y;
    }

    public void update() {
        if (!hasStolenHoney) {
            // Move towards the Bee to steal its Honey
            if (bee.getX_position() > position.x) {
                position.x += 5;
            } else {
                position.x -= 5;
            }
            if (bee.getY_position() > position.y) {
                position.y += 5;
            } else {
                position.y -= 5;
            }
        } else {
            // Get out of the screen when Honey has been stolen
            position.x -= 10;
        }
    }
}

