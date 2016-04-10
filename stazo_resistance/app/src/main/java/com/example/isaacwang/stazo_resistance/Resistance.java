package com.example.isaacwang.stazo_resistance;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Resistance extends Application {
    private Game game;
    private static final String fbURL = "https://amber-torch-3377.firebaseio.com/";
    private Player player;

    public Game getGame() {
        return game;
    }
    public String getFbURL() { return fbURL;}
    public Player getPlayer() {return player;}

    public void setGame(Game game) {
        this.game = game;
    }
    public void setPlayer(Player p) {this.player = p;}

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }
}


