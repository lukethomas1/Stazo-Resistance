package com.example.isaacwang.stazo_resistance;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Resistance extends Application {
    private Game game;
    private static final String fbURL = "https://amber-torch-3377.firebaseio.com/";


    public Game getGame() {
        return game;
    }
    public String getFbURL() { return fbURL;}

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }
}


