package com.example.rayleighs.ballgun;

/**
 * Created by rayleighs on 3/4/16.
 */
public class Bullet {
    float cx, cy;
    float radius;
    float dx = 0, dy = 0;
    boolean fired = false;
    public Bullet(Gun gun) {
        this.radius = 8;
        this.cx = gun.baseX;
        this.cy = gun.baseY;
    }

    public void move(){
        if(fired){
            cx += dx;
            cy += dy;
        }
    }
}
