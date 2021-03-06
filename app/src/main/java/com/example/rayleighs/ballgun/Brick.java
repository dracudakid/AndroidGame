package com.example.rayleighs.ballgun;

import android.graphics.RectF;

/**
 * Created by rayleighs on 3/9/16.
 */
public class Brick {
    float x, y;
    float width, height;
    float dx, dy;

    // keep for the future
    boolean isMoving;
    int screenX, screenY;
    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = width / 10;
        this.dy = 0;
    }

    public RectF getRectF(){
        return new RectF(x, y, x+width, y+height);
    }

}
