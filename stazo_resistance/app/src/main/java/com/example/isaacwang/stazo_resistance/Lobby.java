package com.example.isaacwang.stazo_resistance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Ansel on 4/7/16.
 */
public class Lobby extends AppCompatActivity
{
    private String game_id;
    private Firebase fbRef;
    private Firebase playerRef;
    private ArrayList<Player> playerArray;
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
        fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        playerRef = fbRef.child("games").child(game_id).child("players");
        // Single-execution for adding us to the player array
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Copying the player array
                playerArray = ((ArrayList<Player>) snapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }


    public void addPlayerToGrid( String playerName )
    {
        // Get the grid
        GridLayout grid = (GridLayout) findViewById( R.id.grid );

        TextView playerView = new TextView( this );
        playerView.setText( playerName );

        grid.addView(playerView);
    }

    public void removePlayerFromGrid( String playerName )
    {
        // Get the grid
        GridLayout grid = (GridLayout) findViewById( R.id.grid );

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


}
