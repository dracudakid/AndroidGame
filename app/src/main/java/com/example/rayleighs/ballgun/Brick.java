package com.example.rayleighs.ballgun;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by rayleighs on 3/9/16.
 */
public class Brick {
    float x, y;
    float width, height;
    float dx, dy;
    boolean isMoving;
    Random rand = new Random();

    int screenX, screenY;
    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
//        this.screenX = screenX;
//        this.screenY = screenY;
//        this.y = rand.nextInt(10)*screenY/30;
//        this.x = rand.nextInt(5)*screenX/5;
//        this.width = screenX/5 - 10;
//        this.height = screenY/30 - 10;
    }

    public RectF getRectF(){
        return new RectF(x, y, x+width, y+height);
    }
}
