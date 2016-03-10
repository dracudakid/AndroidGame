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
 * Created by rayleighs on 3/10/16.
 */
public class BGG_Stage2 extends View {
    Paint paint;

    int screenX;
    int screenY;

    /*
    Stage 2 includes 2 marks and 1 brick;
     */
    Brick brick;
    Mark marks[] = new Mark[2];
    Bullet bullets[] = new Bullet[3];
    Gun gun;



    public BGG_Stage2(Context context) {
        super(context);
        paint = new Paint();

        screenX = 720;
        screenY = 1230;

        gun = new Gun(screenX, screenY);
        for(int i=0; i<3; i++){
            bullets[i] = new Bullet(screenX, screenY, i+1);
        }

        marks[0] = new Mark(screenX/4, screenY/4, screenX/8);
        marks[1] = new Mark(screenX*3/5, screenY/5, screenX/10);
        brick = new Brick(screenX/4 + 100, screenY/4 + 100, screenX/4, screenY/32);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // background
        canvas.drawColor(Color.argb(255, 192, 192, 192));


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


        // ve mark
        paint.setColor(Color.WHITE);
        for(int i=0; i< marks.length; i++){
            Mark mark = marks[i];
            if(mark.isVisible == true){
                canvas.drawCircle(mark.cx, mark.cy, mark.radius, paint);
                markCollision(gun.activeBullet, mark);
            }
        }

        // brick1
        paint.setColor(Color.BLACK);
        canvas.drawRect(brick.getRectF(), paint);
        brickCollision(gun.activeBullet, brick);

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
