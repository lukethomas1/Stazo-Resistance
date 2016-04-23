package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Proposal extends AppCompatActivity{

    private Firebase gameRef;
    private Firebase agentsRef;
    private Firebase valsRef;
    private String game_id;
    private ArrayList<Player> playerArray; // pulled from database
    private ArrayList<Player> agentsArray; // stored locally
    private Mission curMission;            // what is the current mission?
    private int proposer_index;            // who is proposing the mission?
    private int memsLeft;                  // how many more members do we need
    private HashMap<String, Integer> vals; // map of values for grabbing data
    private int spyScore, resScore;        // spy and resistance score to be shown at top


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal);

        // firebase reference definitions
        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        gameRef = fbRef.child("games").child(game_id);
        agentsRef = gameRef.child("agents");
        valsRef = gameRef.child("values");

        // initialization
        grabData();
    }

    /**
     * Set text of textview at top to have the correct player's name
     */
    private void setProposerText() {
        ((TextView) findViewById(R.id.proposeMission)).setText(playerArray.get(proposer_index).getName() +
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

    private void grabData() {
        //SINGLE GRAB
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // grabbing the player array
                playerArray = ((ArrayList<Player>) snapshot.child("players").getValue(
                        new GenericTypeIndicator<List<Player>>() {
                        }
                ));

                // setting local agentsArray
                agentsArray = new ArrayList<Player>();

                // grabbing the current mission
                vals = ((HashMap<String, Integer>)
                        snapshot.child("values").getValue(
                                new GenericTypeIndicator<HashMap<String, Integer>>() {
                        }));
                int round = ((Integer) vals.get("round")).intValue();
                ArrayList<Mission> sequence = ((ArrayList<Mission>) snapshot.child("sequence").getValue(
                        new GenericTypeIndicator<List<Mission>>() {
                        }));
                curMission = sequence.get(round);

                // grabbing proposer index
                proposer_index = vals.get("proposer_index");

                // sets proposer and # textviews
                setProposerText();
                setAgentsText();

                // set button names to the names of players
                setNames();

                // set members still needed
                setMemsLeft();

                //grab spy and res score
                spyScore = ((Integer) vals.get("spy_score")).intValue();
                resScore = ((Integer) vals.get("res_score")).intValue();
                // update the score at top
                ((TextView)findViewById(R.id.scoreView)).setText("Agents' Score: " + resScore +
                        " Spies' Score: " + spyScore);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    /**
     * Sets names for all the buttons
     */
    private void setNames() {

        // Set names for buttons 1-5
        ((Button) findViewById(R.id.pButton1)).setText(playerArray.get(0).getName());
        ((Button) findViewById(R.id.pButton2)).setText(playerArray.get(1).getName());
        ((Button) findViewById(R.id.pButton3)).setText(playerArray.get(2).getName());
        ((Button) findViewById(R.id.pButton4)).setText(playerArray.get(3).getName());
        ((Button) findViewById(R.id.pButton5)).setText(playerArray.get(4).getName());

        // Deal with button removal/name setting for buttons 6-10
        removeExtras(playerArray.size());
    }

    /**
     * Removes the extra buttons depending on numPlayers
     * Sets the names for buttons 6-10 if needed
     * Called by setNames
     */
    private void removeExtras(int numPlayers) {
        if (numPlayers < 10) {
            View v = (View) findViewById(R.id.pButton10);
            ((ViewManager)v.getParent()).removeView(v);
        }
        else {
            ((Button) findViewById(R.id.pButton10)).setText(playerArray.get(9).getName());
        }
        if (numPlayers < 9) {
            View v = (View) findViewById(R.id.pButton9);
            ((ViewManager)v.getParent()).removeView(v);
        }
        else {
            ((Button) findViewById(R.id.pButton9)).setText(playerArray.get(8).getName());
        }
        if (numPlayers < 8) {
            View v = (View) findViewById(R.id.pButton8);
            ((ViewManager)v.getParent()).removeView(v);
        }
        else {
            ((Button) findViewById(R.id.pButton8)).setText(playerArray.get(7).getName());
        }
        if (numPlayers < 7) {
            View v = (View) findViewById(R.id.pButton7);
            ((ViewManager)v.getParent()).removeView(v);
        }
        else {
            ((Button) findViewById(R.id.pButton7)).setText(playerArray.get(6).getName());
        }
        if (numPlayers < 6) {
            View v = (View) findViewById(R.id.pButton6);
            ((ViewManager)v.getParent()).removeView(v);
        }
        else {
            ((Button) findViewById(R.id.pButton6)).setText(playerArray.get(5).getName());
        }
    }

    /**
     * Adds player to the mission, response to name being clicked.
     * @param view
     */
    public void togglePlayer(View view) {
        Player player;
        Button button;
        switch(view.getId())
        {
            case R.id.pButton1:
                player = playerArray.get(0);
                button = (Button) findViewById(R.id.pButton1);
                break;
            case R.id.pButton2:
                player = playerArray.get(1);
                button = (Button) findViewById(R.id.pButton2);
                break;
            case R.id.pButton3:
                player = playerArray.get(2);
                button = (Button) findViewById(R.id.pButton3);
                break;
            case R.id.pButton4:
                player = playerArray.get(3);
                button = (Button) findViewById(R.id.pButton4);
                break;
            case R.id.pButton5:
                player = playerArray.get(4);
                button = (Button) findViewById(R.id.pButton5);
                break;
            case R.id.pButton6:
                player = playerArray.get(5);
                button = (Button) findViewById(R.id.pButton6);
                break;
            case R.id.pButton7:
                player = playerArray.get(6);
                button = (Button) findViewById(R.id.pButton7);
                break;
            case R.id.pButton8:
                player = playerArray.get(7);
                button = (Button) findViewById(R.id.pButton8);
                break;
            case R.id.pButton9:
                player = playerArray.get(8);
                button = (Button) findViewById(R.id.pButton9);
                break;
            case R.id.pButton10:
                player = playerArray.get(9);
                button = (Button) findViewById(R.id.pButton10);
                break;
            default:
                player = playerArray.get(0);
                button = (Button) findViewById(R.id.pButton1);
                break;
        }

        /* if the player isn't on the mission already*/
        if (!agentsArray.contains(player)) {

            // if the mission is already full, do nothing
            if (memsLeft == 0) {
                return;
            }

            // change button color to selected
            ((Button)button).setBackground(getDrawable(R.drawable.player_button_selected));

            // add player to mission
            agentsArray.add(player);

            // adjust decrement number of members still needed
            setMemsLeft();
        }

        /* if the player is already on the mission */
        else {

            // change button color to unselected
            ((Button)button).setBackground(getDrawable(R.drawable.player_button));

            // remove player from mission
            agentsArray.remove(player);

            // adjust number of members still needed
            setMemsLeft();
        }

        setAgentsText();
    }

    public void setMemsLeft() {
        memsLeft = curMission.getMems() - agentsArray.size();
    }

    /**
     * Updates database values
     * Proceeds to mission voting screen
     */
    public void done(View view) {
        if (memsLeft == 0) {

            // update Database agents array and proposer_index
            agentsRef.setValue(agentsArray);
            vals.put("proposer_index", proposer_index + 1);
            vals.put("vote_counter", 0);
            vals.put("pro_counter", 0);
            vals.put("proceed_to_vote", 1);
            valsRef.setValue(vals);

            // move on to next activity
            Intent i = new Intent(this, VoteMission.class);
            i.putExtra("game_id", game_id);
            i.putExtra("reset_proceed", true);
            startActivity(i);
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
