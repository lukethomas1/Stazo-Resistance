package com.example.isaacwang.stazo_resistance;

import android.app.Application;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Resistance extends Application {
    private Game game;

    public Game getGame() {return game;}
    public void setGame(Game game) {
        this.game = game;
    }
}
