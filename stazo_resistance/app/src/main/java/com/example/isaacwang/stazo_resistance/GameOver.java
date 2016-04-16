package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameOver extends AppCompatActivity {


    private Firebase fbRef;
    private Firebase playerRef;
    private String winner;
    private String game_id;
    private ArrayList<Player> playerArray;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoverscreen);

        fbRef = new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        playerRef = fbRef.child("games").child(game_id).child("players");
        winner = getIntent().getStringExtra("winner");


        //if resistance wins
        if (winner.equals("Resistance")) {
            ((TextView) findViewById(R.id.wonText)).setText("Resistance wins!");
            iteratePlayers(winner);
            //spies win
        } else {
            ((TextView) findViewById(R.id.wonText)).setText("Spies win!");
            iteratePlayers(winner);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getArray() {

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
    private void iteratePlayers(String str) {
        getArray();
        LinearLayout layout = (LinearLayout) findViewById(R.id.playersList);
        //iterate through array from firebase
        for (Player player : playerArray) {
            //playersList
            TextView name = new TextView(this);
            name.setText(player.getName());
            layout.addView(name);
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GameOver Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.isaacwang.stazo_resistance/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GameOver Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.isaacwang.stazo_resistance/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
