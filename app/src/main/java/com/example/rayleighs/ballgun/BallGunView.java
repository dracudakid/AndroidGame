package com.example.rayleighs.ballgun;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rayleighs on 3/4/16.
 */
public class BallGunView extends View {


    SoundPool soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    int hitMarkID = -1;
    int hitBrickID = -1;

    // bien kiem tra da khoi tao man chua
    boolean isInitialized;

    Paint paint;

    int screenX;
    int screenY;

    Gun gun;
    ArrayList<Bullet> bullets  = new ArrayList<>();

    ArrayList<Mark> marks = new ArrayList<>();
    ArrayList<Brick> bricks = new ArrayList<>();

    int stage = 2;

    public BallGunView(Context context) {
        super(context);
        DisplayMetrics disp = new DisplayMetrics();
                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(disp);
        Log.d("WIDTH_HEIGHT", disp.widthPixels + " " + disp.heightPixels);

        paint = new Paint();
        screenX = disp.widthPixels;
        // screenY in onDraw method is smaller than screenY of disp, dont know why
        screenY = disp.heightPixels - 50;

        // gun is the same for different stages
        gun = new Gun(screenX, screenY);

        for(int i=0; i<3; i++){
            bullets.add(new Bullet(screenX, screenY, i+1));
        }

        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("hitBrick.mp3");
            hitBrickID = soundpool.load(descriptor, 0);
            descriptor = assetManager.openFd("hitMark.mp3");
            hitMarkID = soundpool.load(descriptor, 0);
        }catch (IOException e){

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // background
        canvas.drawColor(Color.argb(255, 192, 192, 192));

        drawComponents(canvas, stage);

        gun.loadBullet(bullets);
        gun.swingToAim();

        marksCollision(gun.activeBullet, marks);
        bricksCollision(gun.activeBullet, bricks);
        checkStageOver();

        if(stage==4) stage = 0;
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    // won the stage or lost the stage
    public void checkStageOver(){
        if(checkWinStage() || checkLoseStage()){
            if(checkWinStage()) stage = stage + 1;
            resetComponents();
            for(int i=0; i<3; i++){
                bullets.add(new Bullet(screenX, screenY, i+1));
            }
            isInitialized = false;
        }
    }

    private boolean checkWinStage(){
        for(int i=0; i<marks.size(); i++){
            if(marks.get(i).isVisible) return false;
        }
        return true;
    }

    private boolean checkLoseStage(){
        for(int i=0; i<bullets.size(); i++){
            Bullet b = bullets.get(i);
            if(b.state != Bullet.OUT) return false;
        }
        return true;
    }

    private void resetComponents(){
        marks.removeAll(marks);
        bricks.removeAll(bricks);
        bullets.removeAll(bullets);
        gun.activeBullet = null;
    }



    private void drawComponents(Canvas canvas, int stage) {
        // initial components of stage if components are not initialized
        if(isInitialized == false){
            initializeComponents(stage);
        }
        drawGunBase(canvas);
        drawBullets(canvas);
        drawMarks(canvas);
        drawBricks(canvas);

    }
    private void initializeComponents(int stage){
        isInitialized = true;
        switch (stage){
            case 1:
                marks.add(new Mark(screenX / 3, screenY / 4, screenX / 10));
                break;
            case 2:
                marks.add(new Mark(screenX/4, screenY/5, screenX/12));
                marks.add(new Mark(screenX*3/4, screenY/4, screenX/12));
                bricks.add(new Brick(screenX / 3 + 50, screenY / 4 + 50, screenX / 4, screenY / 40));
                break;
            case 3:
                marks.add(new Mark(screenX/4, screenY/6, screenX/15));
                marks.add(new Mark(screenX*4/5, screenY/7, screenX/18));
                bricks.add(new Brick(50, screenY / 5 + 50, screenX / 4, screenY / 40));
                bricks.add(new Brick(screenX *3/4, screenY / 5 + 50, screenX / 4, screenY / 40));
        }
    }
    private void drawBricks(Canvas canvas){
        paint.setColor(Color.BLACK);
        for(int i=0; i<bricks.size(); i++){
            Brick b = bricks.get(i);
            canvas.drawRect(b.getRectF(), paint);
        }
    }
    private void drawMarks(Canvas canvas){
        paint.setColor(Color.WHITE);
        for(int i=0; i<marks.size(); i++){
            Mark m = marks.get(i);
            if(m.isVisible){
                canvas.drawCircle(m.cx, m.cy, m.radius, paint);
            }
        }
    }
    private void drawBullets(Canvas canvas) {
        for(int i=0; i< bullets.size(); i++){
            Bullet bullet = bullets.get(i);
            bullet.fly();
            canvas.drawCircle(bullet.cx, bullet.cy, bullet.radius, paint);

        }
    }
    private void drawGunBase(Canvas canvas){
        // gun base
        paint.setColor(Color.argb(255, 200, 255, 180));
        canvas.drawCircle(gun.baseX, gun.baseY, gun.length, paint);
        paint.setColor(Color.BLACK);

        canvas.drawCircle(gun.baseX, gun.baseY, 5, paint);
        canvas.drawLine(gun.baseX, gun.baseY, gun.topX, gun.topY, paint);
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

    private void marksCollision(Bullet bullet, ArrayList<Mark> marks){
        for(int i=0; i<marks.size(); i++){
            markCollision(bullet, marks.get(i));
        }
    }

    private void bricksCollision(Bullet bullet, ArrayList<Brick> bricks){
        for(int i=0; i<bricks.size(); i++){
            brickCollision(bullet, bricks.get(i));
        }
    }
    /*
    private void brickCollision(Bullet bullet, Brick brick){
        if(bullet != null){

            double distanceX = Math.abs((brick.x + brick.width/2) - bullet.cx);
            double distanceY = Math.abs((brick.y + brick.height/2) - bullet.cy);

            if (distanceX > (brick.width/2 + bullet.radius)) { return; }
            if (distanceY > (brick.height/2 + bullet.radius)) { return; }

            if(distanceX <= brick.width/2){
                bullet.dy = - bullet.dy;
                soundpool.play(hitBrickID, 1,1,0,0,1);
                return;
            }
            if(distanceY <= brick.height/2){
                bullet.dx = - bullet.dx;
                soundpool.play(hitBrickID,1,1,0,0,1);
                return;
            }

            double cornerDistance = Math.pow(distanceX - brick.width/2, 2) + Math.pow(distanceY - brick.height/2, 2);
            if(cornerDistance <= bullet.radius * bullet.radius){
                bullet.dx = - bullet.dx;
                bullet.dy = - bullet.dy;
                soundpool.play(hitBrickID,1,1,0,0,1);
            }

        }

    }
*/
    private void brickCollision(Bullet bullet, Brick brick){
        if(bullet != null){
            //check the order up, down, right, left
            if(bullet.cx >= brick.x
                    && bullet.cx <= (brick.x+brick.width)
                    && (bullet.cy-bullet.radius) >= brick.y
                    && (bullet.cy-bullet.radius) <= (brick.y+brick.height))
            {
                // top down
                // bottom up
                bullet.dy = -bullet.dy;
                bullet.cy = brick.y + brick.height + bullet.radius;
                soundpool.play(hitBrickID,1,1,0,0,1);
            }
            else if(bullet.cx>=brick.x && bullet.cx <= (brick.x+brick.width)
                    &&(bullet.cy+bullet.radius) >= brick.y
                    && (bullet.cy+bullet.radius) <= (brick.y+bullet.dy))
            {
                // top down
                bullet.dy = -bullet.dy;
                bullet.cy = brick.y - bullet.radius;
                soundpool.play(hitBrickID,1,1,0,0,1);
            }

            else if(bullet.cy>=brick.y && bullet.cy <= (brick.y+brick.height)
                    && (bullet.cx-bullet.radius >= brick.x+brick.width-bullet.dx)
                    && (bullet.cx-bullet.radius) <= (brick.x+brick.width))
            {
                bullet.dx = -bullet.dx;
                bullet.cx = brick.x + brick.width+bullet.radius;
                soundpool.play(hitBrickID,1,1,0,0,1);
            }
            else if(bullet.cy >= brick.y
                    && bullet.cy<=(brick.y+brick.height)
                    && (bullet.cx+bullet.radius) >= brick.x
                    && (bullet.cx+bullet.radius) <= (brick.x+bullet.dx))
            {
                bullet.dx = -bullet.dx;
                bullet.cx = brick.x - bullet.radius;
                soundpool.play(hitBrickID,1,1,0,0,1);
            }
        }
    }
    private void markCollision(Bullet b, Mark m){
        if(m.isVisible){
            double distance = Math.sqrt((b.cx - m.cx)*(b.cx - m.cx) + (b.cy - m.cy)*(b.cy - m.cy));
            if(distance < b.radius + m.radius){
                soundpool.play(hitMarkID,1,1,0,0,1);
//                m.isVisible = false;
                // collision Point
                float collisionPointX = ((b.cx * m.radius) + (m.cx * b.radius)) / (b.radius + m.radius);
                float collisionPointY = ((b.cy * m.radius) + (m.cy * b.radius)) / (b.radius + m.radius);

                // vector N
                float nVectorX = collisionPointX - m.cx;
                float nVectorY = collisionPointY - m.cy;

                Log.d("OLD", b.dx + " -  " + b.dy);

                float newDX = b.dx + nVectorX;
                float newDY = b.dy + nVectorY;

                /*
                set the length of new V equals to the old one
                1) dx / dy == newDx / newDy
                2) dx^2 + dy^2 == lengthsquare
                3) dx * newDX > 0  // same sign
                4) dy * newDY > 0  // same sign
                 */

                float lengthSquare = b.dx * b.dx + b.dy * b.dy;
                Log.d("NEW", newDX + " ; " + newDY);
                b.dy = (float) Math.sqrt(lengthSquare/(Math.pow(newDX/newDY, 2) + 1));
                b.dx = newDX / newDY * b.dy;

                // check 3) 4) condition
                b.dy = newDY > 0? Math.abs(b.dy) : - Math.abs(b.dy);
                b.dx = newDX > 0? Math.abs(b.dx) : - Math.abs(b.dx);

            }

        }
    }

}
