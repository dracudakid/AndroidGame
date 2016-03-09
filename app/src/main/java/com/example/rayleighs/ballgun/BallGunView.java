package com.example.rayleighs.ballgun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rayleighs on 3/4/16.
 */
public class BallGunView extends View{

    View view;

    volatile boolean playing;
    boolean paused = true;

    Paint paint;

    int screenX;
    int screenY;


    // frame rate
    long fps;

    // calculate the fps
    private long timeThisFrame;
    Bullet bullet;
    Gun gun;


    public BallGunView(Context context) {
        super(context);
        paint = new Paint();

        screenX = 720;
        screenY = 1230;

        gun = new Gun(screenX, screenY);
        bullet = new Bullet(gun);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // background
        canvas.drawColor(Color.argb(255, 255, 255, 255));
        paint.setColor(Color.argb(255, 200, 255, 180));


        // gun base
        canvas.drawCircle(gun.baseX, gun.baseY, gun.length, paint);
        paint.setColor(Color.BLACK);

        canvas.drawCircle(gun.baseX, gun.baseY, 5, paint);

        // bullet
        canvas.drawCircle(bullet.cx, bullet.cy, bullet.radius, paint);

        canvas.drawLine(gun.baseX, gun.baseY, gun.topX, gun.topY, paint);
        Log.d("SIN", Math.sin(Math.PI / 2) + "");

        gun.move();
        bullet.move();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case KeyEvent.ACTION_DOWN:
                gun.stopped = false;
                break;
            case KeyEvent.ACTION_UP:
                gun.stopped = true;
                gun.fire(bullet);
                break;
        }
        return true;
    }
}
