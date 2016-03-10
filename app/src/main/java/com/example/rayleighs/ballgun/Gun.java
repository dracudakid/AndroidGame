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

    boolean stopped = true;

    // so dan da ban
    Bullet activeBullet;


    public Gun(float screenX, float screenY) {
        this.length = screenX/5;

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

    // dat dan chinh (chuan bi ban)
    public void setActiveBullet(Bullet b){
        b.state = Bullet.LOADED;
        b.cx = this.baseX;
        b.cy = this.baseY;
        this.activeBullet = b;
    }

    // nap bang dan
    public void loadBullet(Bullet[] bullets){

        /*
        Kiem tra vien dan hien tai da bi ban hay chua
        Neu ban roi, thi thay vien dan
         */
        if(activeBullet == null || activeBullet.state == Bullet.FIRED){
            for(int i=bullets.length -1; i>=0; i--){
                Bullet b = bullets[i];
                if(b.state == Bullet.FREE){
                    setActiveBullet(b);

                    break;
                }
            }
        }


    }

    // ban
    public void fire(){
        if(activeBullet.state != Bullet.FIRED){
            activeBullet.state = Bullet.FIRED;
            activeBullet.dx = (topX - baseX)/ 5;
            activeBullet.dy = (topY - baseY)/ 5;
        }
    }

}
