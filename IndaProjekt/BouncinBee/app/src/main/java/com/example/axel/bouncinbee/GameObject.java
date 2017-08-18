package com.example.axel.bouncinbee;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by axel on 2017-03-30.
 */

public interface GameObject {

    public void draw(Canvas canvas);
    public void update(Point point);
    public Bitmap getResizedBitmap();
    public float getX_position();
    public float getY_position();

}
