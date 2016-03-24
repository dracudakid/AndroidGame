package com.example.rayleighs.ballgun;

/**
 * Created by Hoai Truong on 3/24/2016.
 */
public class Button {

    float x;
    float y;
    int width;
    int heigh;
    Button(int screenX, int screenY){
        this.x = screenX/2 - screenX/8;
        this.y = screenY/8;
        this.width = screenX/4;
        this.heigh = screenY/7;
    }

    public boolean onTouch_Next(float touchX, float touchY){
        if(touchX>x && touchX<(x+width) && touchY>y && touchY< (y+heigh)) return true;
        return false;
    }
}
