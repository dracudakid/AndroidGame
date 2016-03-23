package com.example.rayleighs.ballgun;

/**
 * Created by rayleighs on 3/4/16.
 */
public class Bullet {
    public static final int FIRED = 2;
    public static final int LOADED = 1;
    public static final int FREE = 0;
    public static final int OUT = 3;

    int screenX, screenY;
    float cx, cy;
    float radius;
    float dx = 0, dy = 0;
    int state = FREE;

    public Bullet(int screenX, int screenY, int index){
        this.screenX = screenX;
        this.screenY = screenY;
        this.cx = index * 50;
        this.cy = screenY - 20;
        this.radius = 8;
    }

    public void fly(){
        if(state == FIRED){
            screenCollision();
            cx += dx;
            cy += dy;

        }
    }

    private void screenCollision(){
        if(cy > screenY){
            this.state = OUT;
        }

        if(cx < 0) cx = 0;
        if(cx > screenX) cx = screenX;
        if(cy < 0) cy = 0;
        if(cy > screenY) cy = screenY;

        if(cx == 0 || cx == screenX){
            dx = - dx;
        }
        if(cy == 0 ){
            dy = - dy;
        }
    }
}
