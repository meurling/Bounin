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

/**
 * Created by axel on 2017-04-06.
 */

public class Flower implements GameObject {

    private Context context;

    private PointF position;

    // Maximum and minimum y-coordinates in between which each Flower can spawn
    float y_Max = Resources.getSystem().getDisplayMetrics().heightPixels - 450;
    float y_Min = Resources.getSystem().getDisplayMetrics().heightPixels * 2/3;

    private float height;
    private float width;

    private Paint paint = new Paint();

    private Bitmap bitmap;
    private Bitmap resizedBitmap;

    Bitmap flower;
    // An array holding the names of flower images in res/drawable/
    String[] images = new String[]{"flower1", "flower2", "flower3", "flower4"};

    private boolean canBounceAgain = true;


    public Flower(Context context, PointF position){
        this.position = position;
        this.context = context;

        // Randomize the initial appearance of the flower
        int index = (int) (Math.random() * (images.length - 1));
        // Get resource ID of image
        int id = context.getResources().getIdentifier(images[index], "drawable", context.getPackageName());
        // Create and resize bitmap
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        this.resizedBitmap = getResizedBitmap(bitmap, (int) (149 * 1.5) , (int) (286 * 1.5));

        flower = this.resizedBitmap;
        height = flower.getHeight();
        width = flower.getWidth();
    }

    public float getX_position(){
        return position.x;
    }

    public float getY_position(){
        return position.y;
    }

    public float getHeight() { return flower.getHeight(); }
    public float getWidth() { return flower.getWidth(); }


    public Bitmap getResizedBitmap(){
        return resizedBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    public boolean canBounce(float x_position, float y_position, float beeWidth, float beeHeight) {
        if (canBounceAgain) {
            float beeCenter = beeWidth / 2;

            if ((x_position + beeCenter >= getX_position()) && (x_position + beeCenter <= getX_position() + width)) {
                if ((y_position + beeHeight >= getY_position()) && (y_position + beeHeight <= getY_position() + 60)) {
                    canBounceAgain = false;
                    return true;
                }
            }
        }
        return false;
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
        position.x -= 10;
    }

    //method that is called in order to reset the flowers once they are outside of the screen
    public void resetFlower(float newPosition){
        position.x = newPosition;
        randomizeHeight();
        canBounceAgain = true;
        randomizeAppearance();
    }

    public void randomizeAppearance() {
        // Randomly choose one of the names of images in list images
        int index = (int) (Math.random() * (images.length - 1));
        // Get resource ID of image
        int id = context.getResources().getIdentifier(images[index], "drawable", context.getPackageName());
        // Create and resize bitmap of chosen image
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        this.resizedBitmap = getResizedBitmap(bitmap, (int) (149 * 1.5) , (int) (286 * 1.5));

        flower = this.resizedBitmap;
        height = flower.getHeight();
        width = flower.getWidth();
    }

    public void randomizeHeight() {
        position.y = y_Min + (float) (Math.random() * (y_Max - y_Min));
    }
}
