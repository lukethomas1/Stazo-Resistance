package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Proposal extends AppCompatActivity{
    private Game game;
    private int memsLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal);
        game = getGame();
        memsLeft = game.getMission().getMems();

        //Resets agentIndex, "clearing" the agents
        game.clearAgents();

        //Sets proposer and # textviews
        setProposerText();
        setAgentsText();

        //Set button names to the names of players
        setNames();

        //Remove extra buttons for < 10 players
        removeExtras();
    }

    /**
     * Set text of textview at top to have the correct player's name
     */
    private void setProposerText() {
        ((TextView) findViewById(R.id.proposeMission)).setText(game.getProposer().getName() +
                ", propose a Mission!");
    }

    /**
     * Sets # of mems left text field
     */
    private void setAgentsText() {
        if (memsLeft == 0) {
            ((TextView)findViewById(R.id.membersNeeded)).setText("Mission ready! Hit \"Done\" " +
                    "to proceed");
        }
        else {
            ((TextView) findViewById(R.id.membersNeeded)).setText("You need "
                    + memsLeft + " more agents");
        }
    }

    /**
     * Sets names for all the buttons
     */
    private void setNames() {
        ((Button)findViewById(R.id.pButton1)).setText(game.getPlayerName(0));
        ((Button)findViewById(R.id.pButton2)).setText(game.getPlayerName(1));
        ((Button)findViewById(R.id.pButton3)).setText(game.getPlayerName(2));
        ((Button)findViewById(R.id.pButton4)).setText(game.getPlayerName(3));
        ((Button)findViewById(R.id.pButton5)).setText(game.getPlayerName(4));
        ((Button)findViewById(R.id.pButton6)).setText(game.getPlayerName(5));
        ((Button)findViewById(R.id.pButton7)).setText(game.getPlayerName(6));
        ((Button)findViewById(R.id.pButton8)).setText(game.getPlayerName(7));
        ((Button)findViewById(R.id.pButton9)).setText(game.getPlayerName(8));
        ((Button)findViewById(R.id.pButton10)).setText(game.getPlayerName(9));
    }

    /**
     * Removes the extra buttons depending on numPlayers
     */
    private void removeExtras() {
        if (game.getNumPlayers() < 10) {
            View v = (View) findViewById(R.id.pButton10);
            ((ViewManager)v.getParent()).removeView(v);
        }
        if (game.getNumPlayers() < 9) {
            View v = (View) findViewById(R.id.pButton9);
            ((ViewManager)v.getParent()).removeView(v);
        }
        if (game.getNumPlayers() < 8) {
            View v = (View) findViewById(R.id.pButton8);
            ((ViewManager)v.getParent()).removeView(v);
        }
        if (game.getNumPlayers() < 7) {
            View v = (View) findViewById(R.id.pButton7);
            ((ViewManager)v.getParent()).removeView(v);
        }
        if (game.getNumPlayers() < 6) {
            View v = (View) findViewById(R.id.pButton6);
            ((ViewManager)v.getParent()).removeView(v);
        }
    }

    /**
     * Retrieves game from application
     * @return the game
     */
    private Game getGame() {
        // Retrieve the Resistance application
        Resistance resistance = (Resistance) getApplication();

        // Retrieve the game
        Game game = resistance.getGame();

        return game;
    }

    /**
     * Adds player to the mission, response to name being clicked.
     * @param view
     */
    public void addPlayer(View view) {
        Player player;
        Button button;
        switch(view.getId())
        {
            case R.id.pButton1:
                player = game.getPlayer(0);
                button = (Button) findViewById(R.id.pButton1);
                break;
            case R.id.pButton2:
                player = game.getPlayer(1);
                button = (Button) findViewById(R.id.pButton2);
                break;
            case R.id.pButton3:
                player = game.getPlayer(2);
                button = (Button) findViewById(R.id.pButton3);
                break;
            case R.id.pButton4:
                player = game.getPlayer(3);
                button = (Button) findViewById(R.id.pButton4);
                break;
            case R.id.pButton5:
                player = game.getPlayer(4);
                button = (Button) findViewById(R.id.pButton5);
                break;
            case R.id.pButton6:
                player = game.getPlayer(5);
                button = (Button) findViewById(R.id.pButton6);
                break;
            case R.id.pButton7:
                player = game.getPlayer(6);
                button = (Button) findViewById(R.id.pButton7);
                break;
            case R.id.pButton8:
                player = game.getPlayer(7);
                button = (Button) findViewById(R.id.pButton8);
                break;
            case R.id.pButton9:
                player = game.getPlayer(8);
                button = (Button) findViewById(R.id.pButton9);
                break;
            case R.id.pButton10:
                player = game.getPlayer(9);
                button = (Button) findViewById(R.id.pButton10);
                break;
            default:
                player = game.getPlayer(0);
                button = (Button) findViewById(R.id.pButton1);
                break;
        }
        if (!game.isOnMission(player)) {
            ((Button)button).setBackgroundColor(getColor(R.color.colorPressed));
        }
        game.addToMission(player);
        memsLeft--;
        if (memsLeft >= 0) {
            setAgentsText();
        }
    }

    public void done(View view) {
        if (game.missionReady()) {
            Intent i = new Intent(this, Approval.class);
            startActivity(i);
        }
    }
}
