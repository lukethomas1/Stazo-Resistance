package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameOver extends AppCompatActivity {


    private Firebase fbRef;
    private Firebase playerRef;
    private String winner;
    private String game_id;
    private ArrayList<Player> playerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoverscreen);

        fbRef = new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        playerRef = fbRef.child("games").child(game_id).child("players");
        winner = getIntent().getStringExtra("winner");


        //if resistance wins
        if(winner.equals("Resistance")){
            ((TextView) findViewById(R.id.wonText)).setText("Resistance wins!");
            iteratePlayers(winner);
        //spies win
        } else {
            ((TextView) findViewById(R.id.wonText)).setText("Spies win!");
            iteratePlayers(winner);
        }
    }

    private void getArray(){

        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // grabbing the player array
                playerArray = ((ArrayList<Player>) snapshot.child("players").getValue(
                        new GenericTypeIndicator<List<Player>>() {
                        }
                ));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    /*
     * iterates through list of players and
     * dynamically creates textviews for players who have won.
     */
    private void iteratePlayers(String str){
        getArray();
        //iterate through array from firebase
        for (Player player : playerArray){
            //dynamically create texviews to be shown...
        }
    }

    @Override
    public void onBackPressed() {
        String[] quitArray = {"Yes", "No"};
        AlertDialog.Builder numEntry = new AlertDialog.Builder(this);
        numEntry.setTitle("Are you sure you want to quit this game?");
        numEntry.setItems(quitArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (whichButton == 1)
                    dialog.cancel();
                else
                    goToIntro();
            }
        });
        numEntry.create();
        numEntry.show();
    }

    public void goToIntro() {
        Intent i = new Intent(this, IntroActivity.class);
        startActivity(i);
    }

}
