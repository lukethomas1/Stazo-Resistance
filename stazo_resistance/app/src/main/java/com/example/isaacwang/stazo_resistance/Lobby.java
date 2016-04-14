package com.example.isaacwang.stazo_resistance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ansel on 4/7/16.
 */
public class Lobby extends AppCompatActivity
{
    private String game_id;
    private Firebase gameRef;
    //private Firebase playerRef;
    private ArrayList<Player> playerArray;
    private int numPlayers;

    // Sequences for missions depending on number of players
    private static final Mission[] fiveSequence = {new Mission(2,1), new Mission(3,1),
            new Mission(2,1), new Mission(3,1), new Mission(3,1)};
    private static final Mission[] sixSequence = {new Mission(2,1), new Mission(3,1),
            new Mission(4,1), new Mission(3,1), new Mission(4,1)};
    private static final Mission[] sevenSequence = {new Mission(2,1), new Mission(3,1),
            new Mission(3,1), new Mission(4,2), new Mission(4,1)};
    private static final Mission[] entSequence = {new Mission(3,1), new Mission(4,1),
            new Mission(4,1), new Mission(5,2), new Mission(5,1)};

    // Array of all the sequences
    private static final Mission[][] allSequences = {fiveSequence, sixSequence, sevenSequence,
            entSequence, entSequence, entSequence};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.lobby);

        //addPlayerToGrid("Ansel");
        //addPlayerToGrid("Matt");
        //addPlayerToGrid("Luke");
        this.game_id = getIntent().getStringExtra("game_id");
        ((TextView) findViewById(R.id.idTextView)).setText(game_id);

        // Changes to Lobby handling
        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        gameRef = fbRef.child("games").child(game_id);
        Firebase playerRef = gameRef.child("players");
        Firebase readyRef = gameRef.child("values").child("ready");

        // Adding players to the player array
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Copying the player array
                playerArray = ((ArrayList<Player>)
                        snapshot.getValue(new GenericTypeIndicator<List<Player>>() {
                        }));

                // updating number of players
                numPlayers = playerArray.size();

                // displaying the names
                for (Player p : playerArray) {
                    addPlayerToGrid(p.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        readyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (((Integer) dataSnapshot.getValue(Integer.class)).intValue() == 1) {
                    goToProposal();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void addPlayerToGrid( String playerName )
    {
        // no duplicate additions
        if (gridContainsPlayer(playerName)) {
            return;
        }
        // Get the grid
        LinearLayout grid = (LinearLayout) findViewById( R.id.player_container );

        TextView playerView = new TextView( this );

        playerView.setGravity(Gravity.CENTER); // Center vertically and horizontally

        playerView.setText(playerName);

        grid.addView(playerView);
    }

    // is this name already in the grid?
    public boolean gridContainsPlayer(String name) {
        // Get the grid
        LinearLayout grid = (LinearLayout) findViewById( R.id.player_container );

        for ( int i = 0; i < grid.getChildCount(); i++ )
        {
            // Get the view at the current index
            TextView playerView = (TextView) grid.getChildAt( i );

            // Check if the player name is equal to the one to remove
            if ( playerView.getText().equals(name ) )
            {
                return true;
            }
        }
        return false;
    }

    public void removePlayerFromGrid( String playerName )
    {
        // Get the grid
        LinearLayout grid = (LinearLayout) findViewById( R.id.player_container );

        for ( int i = 0; i < grid.getChildCount(); i++ )
        {
            // Get the view at the current index
            TextView playerView = (TextView) grid.getChildAt( i );

            // Check if the player name is equal to the one to remove
            if ( playerView.getText().equals( playerName ) )
            {
                grid.removeViewAt( i );

                break;
            }
        }
    }

    // initializes game values
    public void startGame(View view) {

        // the sequence of missions stored on database
        gameRef.child("sequence").setValue(getMissionSequence());

        // we are ready for the game to start -> chain into goToProposal
        gameRef.child("values").child("ready").setValue(new Integer(1));

        gameRef.child("values").child("num_players").setValue(new Integer(numPlayers));
    }

    // called when ready is set to 1;
    public void goToProposal() {
        Intent i = new Intent(this, Proposal.class);
        i.putExtra("game_id", game_id);
        startActivity(i);
    }

    public Mission[] getMissionSequence() {
        return allSequences[numPlayers-5];
        //return allSequences[0];
    }


}
