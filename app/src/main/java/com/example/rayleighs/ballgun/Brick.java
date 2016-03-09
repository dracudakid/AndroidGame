package com.example.rayleighs.ballgun;

import java.util.Random;

/**
 * Created by rayleighs on 3/9/16.
 */
public class Brick {
    float x, y;
    float width, length;
    float dx, dy;
    boolean isMoving;
    Random rand = new Random();

    int screenX, screenY;
    public Brick(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.y = rand.nextInt(10)*screenY/30;
        this.x = rand.nextInt(5)*screenX/5;
        this.width = screenX/5 - 10;
        this.length = screenY/30 - 10;
    }
}
