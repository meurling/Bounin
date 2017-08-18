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

/**
 * Created by cajsapierrou on 2017-05-10.
 */

    public class Honey implements GameObject {

        private Bitmap bitmap;
        private Bitmap resizedBitmap;
        private Paint paint = new Paint();

        private PointF position;

        private boolean consumed;

        //get the individual phone size
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float screenHeight = metrics.heightPixels;


        public Honey(Context context, PointF position) {

            this.position = position;

            this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.honey);
            this.resizedBitmap = getResizedBitmap(bitmap, 150, 150);

            consumed = false;
        }

        public void consume() {
            consumed = true;
        }

        public void unConsume() {
            consumed = false;
        }

        public boolean isConsumed() {
            return consumed;
        }

        public void setX_position(float x_cord) {
            position.x = x_cord;
        }

        public void randomizeY_position() {
            position.y = (float) Math.random() * screenHeight;
        }

        public float getX_position() {
            return position.x;
        }

        public float getY_position() {
            return position.y;
        }

        public Bitmap getResizedBitmap() {
            return resizedBitmap;
        }

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
            position.x -= 10;
        }
    }
