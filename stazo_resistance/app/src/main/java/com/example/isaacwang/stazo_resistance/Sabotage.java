package com.example.isaacwang.stazo_resistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sabotage extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabotage);

        Resistance resistance = ((Resistance) this.getApplication());
        game = resistance.getGame();
    }


}
