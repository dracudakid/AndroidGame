package com.example.rayleighs.ballgun;

/**
 * Created by rayleighs on 3/4/16.
 */
public class Bullet {
    float cx, cy;
    float radius;
    boolean fired = false;
    public Bullet(Gun gun) {
        this.radius = 10;
        this.cx = gun.baseX;
        this.cy = gun.baseY;
    }

    public void move(){
    }
}
