package com.example.rayleighs.ballgun;

/**
 * Created by rayleighs on 3/10/16.
 */
public class Mark {
    int screenX;
    int screenY;
    float cx, cy;
    float radius;
    float dx, dy;

    boolean isVisible = true;

    public Mark(int x, int y, int radius) {
        this.cx = x;
        this.cy = y;
        this.radius = radius;

    }

}
