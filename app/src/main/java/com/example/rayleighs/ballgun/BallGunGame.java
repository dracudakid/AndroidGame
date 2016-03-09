package com.example.rayleighs.ballgun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BallGunGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BallGunView bgView = new BallGunView(this);
        setContentView(bgView);
    }
}
