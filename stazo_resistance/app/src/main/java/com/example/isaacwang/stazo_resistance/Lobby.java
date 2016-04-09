package com.example.isaacwang.stazo_resistance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by Ansel on 4/7/16.
 */
public class Lobby extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.lobby);

        addPlayerToGrid("Ansel");
        addPlayerToGrid("Matt");
        addPlayerToGrid("Luke");
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
