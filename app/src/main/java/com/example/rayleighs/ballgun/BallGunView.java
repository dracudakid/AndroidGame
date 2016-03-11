package com.example.rayleighs.ballgun;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rayleighs on 3/4/16.
 */
public class BallGunView extends View{

//
//    volatile boolean playing;
//    boolean paused = true;
//    // frame rate
//    long fps;
//    // calculate the fps
//    private long timeThisFrame;

    SoundPool soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    int loseLifeID = -1;
    int explodeID = -1;

    // bien kiem tra da khoi tao man chua
    boolean isInitialized;

    Paint paint;

    int screenX;
    int screenY;

    Gun gun;
    ArrayList<Bullet> bullets  = new ArrayList<>();

    ArrayList<Mark> marks = new ArrayList<>();
    ArrayList<Brick> bricks = new ArrayList<>();

    int stage = 1;

    public BallGunView(Context context) {
        super(context);
        paint = new Paint();
        screenX = 480;
        screenY = 750;
        // gun is the same for different stages
        gun = new Gun(screenX, screenY);

        for(int i=0; i<3; i++){
            bullets.add(new Bullet(screenX, screenY, i+1));
        }

        try{
            // am thanh
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = soundpool.load(descriptor, 0);
            descriptor = assetManager.openFd("explode.ogg");
            explodeID = soundpool.load(descriptor, 0);
        }catch (IOException e){

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // background
        canvas.drawColor(Color.argb(255, 192, 192, 192));

        // khoi tao cac thanh phan cua mang
        initStage(stage);

        // ve cac thanh phan da khoi tao
        generateStage(canvas);

        // nap dan
        gun.loadBullet(bullets);

        // di chuyen sung
        gun.move();

        // kiem tra va cham voi dich va gach
        marksCollision(gun.activeBullet, marks);
        bricksCollision(gun.activeBullet, bricks);

        // kiem tra qua mang
        stageUp();

        // kiem tra neu thua
        resetStage();

        if(stage==4) stage = 0;
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    public void resetStage(){
        for(int i=0; i<bullets.size(); i++){
            Bullet b = bullets.get(i);
            if(b.state != Bullet.OUT) return;
        }
        resetComponents();
        for(int i=0; i<3; i++){
            bullets.add(new Bullet(screenX, screenY, i+1));
        }
        isInitialized = false;
    }

    public void stageUp(){
        for(int i=0; i<marks.size(); i++){
            if(marks.get(i).isVisible) return;
        }
        resetComponents();
        for(int i=0; i<3; i++){
            bullets.add(new Bullet(screenX, screenY, i+1));
        }
        isInitialized = false;
        stage = stage + 1;
    }

    private void resetComponents(){
        marks.removeAll(marks);
        bricks.removeAll(bricks);
        bullets.removeAll(bullets);
        gun.activeBullet = null;
    }

    public void initStage(int stage){
        if(isInitialized == false){
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


    }

    private void generateStage(Canvas canvas) {
        drawGunBase(canvas);
        drawBullets(canvas);
        drawMarks(canvas);
        drawBricks(canvas);

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
            canvas.drawCircle(bullet.cx, bullet.cy, bullet.radius, paint);
            bullet.move();
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
    private void brickCollision(Bullet bullet, Brick brick){
        if(bullet != null){
            double distanceX = Math.abs((brick.x + brick.width/2) - bullet.cx);
            double distanceY = Math.abs((brick.y + brick.height/2) - bullet.cy);

            if (distanceX > (brick.width/2 + bullet.radius)) { return; }
            if (distanceY > (brick.height/2 + bullet.radius)) { return; }

            if(distanceX <= brick.width/2){
                bullet.dy = - bullet.dy;
                soundpool.play(loseLifeID, 1,1,0,0,1);
                return;
            }
            if(distanceY <= brick.height/2){
                bullet.dx = - bullet.dx;
                soundpool.play(loseLifeID,1,1,0,0,1);
                return;
            }

            double cornerDistance = Math.pow(distanceX - brick.width/2, 2) + Math.pow(distanceY - brick.height/2, 2);
            if(cornerDistance <= bullet.radius * bullet.radius){
                bullet.dx = - bullet.dx;
                bullet.dy = - bullet.dy;
                soundpool.play(loseLifeID,1,1,0,0,1);
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
                soundpool.play(explodeID,1,1,0,0,1);
            }

        }

    }
}
