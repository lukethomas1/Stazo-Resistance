package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProposalInactive extends AppCompatActivity {

    private Firebase gameRef;
    private String game_id;
    private String playerName;
    private HashMap<String, Integer> vals;
    private int spyScore, resScore;        // spy and resistance score to be shown at top

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_inactive);
        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        gameRef = fbRef.child("games").child(game_id);
        grabData(); // constant listener
    }

    private void grabData() {
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                vals = ((HashMap<String, Integer>)
                        snapshot.child("values").getValue(
                                new GenericTypeIndicator<HashMap<String, Integer>>() {
                                }));

                int proposer_index = ((Integer) vals.get("proposer_index")).intValue();

                // grabbing the proposing player

                ArrayList<Player> playerArray = (ArrayList<Player>)
                        snapshot.child("players").getValue(new GenericTypeIndicator<List<Player>>() {
                        });

                playerName = playerArray.get(proposer_index).getName();
                System.out.println(playerName);
                ((TextView)findViewById(R.id.proposing)).setText(playerName + " is proposing...");

                // has the proposer finished?
                if (vals.get("proceed_to_vote").intValue() == 1) {
                    //remove listener
                    gameRef.removeEventListener(this);
                    proceedToVote();
                }
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

    public void proceedToVote(){


        // move on to next activity
        Intent i = new Intent(this, VoteMission.class);
        i.putExtra("game_id", game_id);
        i.putExtra("reset_proceed", false); // we are not proposer so let that guy do it
        startActivity(i);
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
