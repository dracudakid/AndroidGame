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

    public void move(int screenX, int screenY){
        if(fired){
            // xoa va cham
            if(cx < 0) cx = 0;
            if(cx > screenX) cx = screenX;
            if(cy < 0) cy = 0;
            if(cy > screenY) cy = screenY;

            // kiem tra va cham
            if(cx == 0 || cx == screenX){
                dx = - dx;
            }
            if(cy == 0 || cy == screenY){
                dy = - dy;
            }
            cx += dx;
            cy += dy;

        }
    }
}
