package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class IntroActivity extends AppCompatActivity {

    private Firebase fbRef;
    private String game_id;
    private ArrayList<Player> p;
    private Firebase playerRef;
    private boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introscreen);
        fbRef = new Firebase(((Resistance) getApplication()).getFbURL());
    }

    /**
     * Creates game directory on database an d initializes player array
     * with player 1 and moves to the lobby screen
     * @param view
     */
    public void startGame(View view){

        // Creating game on database
        game_id =  generateGameId();

        playerRef = fbRef.child("games").child(game_id).child("players");

        // Creating players array in game and adding player 1
        p = new ArrayList<Player>();
        Player me = new Player("Player 1", 1);
        p.add(me);
        playerRef.setValue(p);

        // Save payer to application too
        Resistance game = ((Resistance) getApplication());
        game.setPlayer(me);

        // Values hashmap initialization
        HashMap<String, Integer> values = new HashMap<String, Integer>();
        values.put("proposer_index", new Integer(0));
        values.put("spy_score", new Integer(0));
        values.put("res_score", new Integer(0));
        values.put("round", new Integer(0));
        values.put("proceed_to_proposal", new Integer(0));
        values.put("proceed_to_vote", new Integer(0));
        values.put("fail_counter", new Integer(0)); // Used in MissionActiveActivity
        values.put("voter_turnout", new Integer(0)); // Used in MissionActiveActivity
        fbRef.child("games").child(game_id).child("values").setValue(values);

        // Starting Lobby activity
        Intent toLobby = new Intent( this, Lobby.class );
        toLobby.putExtra("game_id", game_id);
        startActivity( toLobby );
    }

    /**
     * Adds player to the player array for corresponding game and moves to
     * the lobby screen
     * @param view
     */
    public void joinGame(View view) {
        // Name entry dialog
        AlertDialog.Builder gameIdEntry = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);

        gameIdEntry.setTitle("Enter the game code");
        gameIdEntry.setView(input);
        gameIdEntry.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Finding the game
                game_id = (input.getText().toString());
                if (!game_id.isEmpty()){setupGamesListener();}
                }
        });
        gameIdEntry.show();
    }

    public void URLDoesNotExist(String gameID) {
        String[] quitArray = {"Okay"};
        AlertDialog.Builder numEntry = new AlertDialog.Builder(this);

        numEntry.setTitle("The game " + gameID + " you entered does not exist. Try Again!");
        numEntry.setItems(quitArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        numEntry.create();
        numEntry.show();
    }

    public String generateGameId() {
        Random rand = new Random();
        int a;
        String code = "";
        for (int i=0; i < 6; i++ ) {
            a = rand.nextInt(10);
            code += a;
        }
        return code;
    }

    private void setupGamesListener() {
        Firebase gameDirectoryRef = fbRef.child("games");

        //find game id within dir
        gameDirectoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                exists = snapshot.hasChild(game_id);
                //check if the game id exists
                System.out.println("exists:" + exists);
                if(exists) {

                    // Single-execution for adding us to the player array
                    playerRef = fbRef.child("games").child(game_id).child("players");
                    playerRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // Adding us to the player array
                            int id = ((ArrayList<Player>) snapshot.getValue()).size() + 1;
                            Player me = new Player("Player " + id, id);

                            p = ((ArrayList<Player>) snapshot.getValue());
                            p.add(me);
                            playerRef.setValue(p);

                            // Save player to application too
                            Resistance game = ((Resistance)getApplication());
                            game.setPlayer(me);

                            // Starting Lobby activity
                            Intent toLobby = new Intent(getApplicationContext(), Lobby.class );
                            toLobby.putExtra("game_id", game_id);
                            startActivity(toLobby);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
                else {URLDoesNotExist(game_id);}

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }

        });
    }

    public Boolean hasPlayer(String id) {
        return true;
    }

    public void howToPlay (View view) {
        Intent intent = new Intent(this, HowToPlay.class);
        startActivity(intent);
    }

    public void goToOptions(View view) {
        Intent i = new Intent(this, OptionsActivity.class);
        startActivity(i);
    }
}
