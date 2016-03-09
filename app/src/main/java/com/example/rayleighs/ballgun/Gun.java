package com.example.rayleighs.ballgun;

import android.graphics.RectF;

/**
 * Created by rayleighs on 3/4/16.
 */
public class Gun {
    private RectF rect;

    // chieu dai sung
    float length;

    // toa do nong sung
    float topX, topY;

    // toa do diem dat sung
    float baseX, baseY;

    double angle;
    double speed;

    boolean stopped;

    public Gun(float screenX, float screenY) {
        this.length = screenX/4;

        this.baseX = screenX/2;
        this.baseY = screenY;
        this.topX = screenX/2;
        this.topY = this.baseY - this.length;
        this.angle = Math.PI / 2;
        this.speed = Math.PI/32;
    }

    public void move(){
        if(!stopped){
            if(angle > Math.PI) angle = Math.PI;
            if(angle < 0) angle = 0;
            if(angle == Math.PI || angle == 0) speed = -speed;
            angle = angle + speed;
            topX =(float) (baseX + length*Math.cos(angle));
            topY =(float) (baseY - length*Math.sin(angle));
        }
    }

}
