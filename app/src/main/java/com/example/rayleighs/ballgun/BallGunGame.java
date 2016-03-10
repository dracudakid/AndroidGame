package com.example.rayleighs.ballgun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BallGunGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGG_Stage2 bgView = new BGG_Stage2(this);
        setContentView(bgView);
    }
}
