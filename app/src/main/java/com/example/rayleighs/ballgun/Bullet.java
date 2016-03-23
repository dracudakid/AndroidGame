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
            cx += dx;
            cy += dy;
            screenCollision();
        }
    }

    private void screenCollision(){
        if(cy > screenY){
            this.state = OUT;
        }

        if(cx - radius < 0) cx = radius;
        if(cx + radius > screenX) cx = screenX - radius;
        if(cy - radius < 0) cy = radius;
//        if(cy + radius > screenY) cy = screenY -radius;

        if(cx -radius == 0 || cx + radius == screenX){
            dx = - dx;
        }
        if(cy - radius == 0 ){
            dy = - dy;
        }
    }
}
