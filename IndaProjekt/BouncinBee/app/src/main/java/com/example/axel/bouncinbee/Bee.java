package com.example.axel.bouncinbee;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Created by axel on 2017-04-06.
 */

public class Bee implements GameObject {

    private Bitmap bitmap;
    private Bitmap resizedBitmap;
    private Paint paint = new Paint();

    private PointF position;

    private float speed_y;
    private boolean canBoost = true;

    //get the individual phone size
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    float screenWidth = metrics.widthPixels;
    float screenHight = metrics.heightPixels;


    //Constructor
    public Bee(Context context, PointF position){
        this.position = position;
        //this.x_position = x_cord;
        //this.y_position = y_cord;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee3);
        this.resizedBitmap = getResizedBitmap(bitmap, 250, 250);
    }

    public float getX_position(){
        return position.x;
    }
    public float getY_position(){
        return position.y;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getHeight() { return resizedBitmap.getHeight(); }
    public float getWidth() { return resizedBitmap.getWidth(); }

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

    public void update(){
        position.y += 15 + speed_y;
        if (speed_y < 0){
            speed_y += 2;
        }
    }

    public void bounce(){
        speed_y = -50;
        canBoost = true;
    }

    public void boostSpeed(){
        if (canBoost) {
            speed_y = -49;
            canBoost = false;
        }
    }

    public void setCanBoost(boolean sanning){
        canBoost = sanning;
    }
}
