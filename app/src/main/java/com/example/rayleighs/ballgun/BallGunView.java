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
    Bullet bullets[] = new Bullet[3];
    Gun gun;
    Mark mark;


    public BallGunView(Context context) {
        super(context);
        paint = new Paint();

        screenX = 720;
        screenY = 1230;

        gun = new Gun(screenX, screenY);
        mark = new Mark(screenY/3, screenX/3, screenX/6);


        for(int i=0; i<3; i++){
            bullets[i] = new Bullet(screenX, screenY, i+1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // background
        canvas.drawColor(Color.argb(255, 192, 192, 192));

        paint.setColor(Color.WHITE);
        if(mark.isVisible == true){
            canvas.drawCircle(mark.cx, mark.cy, mark.radius, paint);
        }

        // brick
        paint.setColor(Color.BLACK);
//        for(int i=0; i<bricks.length; i++){
//            Brick brick = bricks[i];
//            canvas.drawRect(new RectF(brick.x, brick.y, brick.x+brick.width, brick.y+brick.height), paint);
//            brickCollision(gun.activeBullet, brick);
//        }


        // gun base
        paint.setColor(Color.argb(255, 200, 255, 180));
        canvas.drawCircle(gun.baseX, gun.baseY, gun.length, paint);
        paint.setColor(Color.BLACK);

        canvas.drawCircle(gun.baseX, gun.baseY, 5, paint);

        for(int i=0; i< bullets.length; i++){
            Bullet bullet = bullets[i];
            canvas.drawCircle(bullet.cx, bullet.cy, bullet.radius, paint);
            bullet.move();
        }


        canvas.drawLine(gun.baseX, gun.baseY, gun.topX, gun.topY, paint);
        Log.d("SIN", Math.sin(Math.PI / 2) + "");

        gun.move();
        gun.loadBullet(bullets);
        markCollision(gun.activeBullet, mark);

        try {
            Thread.sleep(30);
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
                gun.fire();
                break;
        }
        return true;
    }

    private void brickCollision(Bullet bullet, Brick brick){
        if(bullet != null){
            double distanceX = Math.abs((brick.x + brick.width/2) - bullet.cx);
            double distanceY = Math.abs((brick.y + brick.height/2) - bullet.cy);

            if (distanceX > (brick.width/2 + bullet.radius)) { return; }
            if (distanceY > (brick.height/2 + bullet.radius)) { return; }

            if(distanceX <= brick.width/2){
                bullet.dy = - bullet.dy;
                return;
            }
            if(distanceY <= brick.height/2){
                bullet.dx = - bullet.dx;
                return;
            }

            double cornerDistance = Math.pow(distanceX - brick.width/2, 2) + Math.pow(distanceY - brick.height/2, 2);
            if(cornerDistance <= bullet.radius * bullet.radius){
                bullet.dx = - bullet.dx;
                bullet.dy = - bullet.dy;
            }

        }

    }

    private void markCollision(Bullet b, Mark m){
        if(m.isVisible){
            double d = Math.sqrt((b.cx - m.cx)*(b.cx - m.cx) + (b.cy - m.cy)*(b.cy - m.cy));
            if(d <= b.radius + m.radius){
                m.isVisible = false;
                if(b.dx * m.dx < 0 && b.dy * m.dy < 0){
                    b.dx = - b.dx;
                    b.dy = - b.dy;
                    m.dx = - m.dx;
                    m.dy = - m.dy;
                } else if(b.dx * m.dx < 0){
                    b.dx =- b.dx;
                    m.dx = - m.dx;
                } else if(b.dy * m.dy < 0){
                    b.dy = - b.dy;
                    m.dy = - m.dy;
                } else{
                    b.dx = - b.dx;
                    b.dy = - b.dy;
                    m.dx = - m.dx;
                    m.dy = - m.dy;
                }
            }
        }

    }
}
