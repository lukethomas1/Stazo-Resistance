package com.example.isaacwang.stazo_resistance;

import android.app.Application;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Resistance extends Application {
    private static final String fbURL = "https://amber-torch-3377.firebaseio.com/";

    // locally stored player array, initialized after lobby goes.
    private ArrayList<Player> playerArray = new ArrayList<Player>();

    // your own id
    private int myId;



    public String getFbURL() { return fbURL;}

    public ArrayList<Player> getPlayerArray() {
        return playerArray;
    }

    public void setPlayerArray(ArrayList<Player> p) {
        playerArray = p;
    }

    public void setMyId(int id) {
        myId = id;
    }
    public int getMyId() {
        return myId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }
}


