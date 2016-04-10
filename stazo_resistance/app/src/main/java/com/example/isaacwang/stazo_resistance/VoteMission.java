package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ericzhang on 4/8/16.
 */
public class VoteMission extends AppCompatActivity{

    private ArrayList<Player> agentArray;
    private int voteCounter;
    private int proCounter;
    private Firebase gameRef =
            (new Firebase(((Resistance) getApplication()).getFbURL())).child("games").child("game_id");
    HashMap<String, Integer> values;

    private String game_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_mission);

        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        gameRef = fbRef.child("games").child(game_id);

        //Listener for agents and values
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Copying the agent array
                agentArray = ((ArrayList<Player>) snapshot.child("agents").getValue(new GenericTypeIndicator<ArrayList<Player>>(){}));

                //Get the list of agents to display for people to vote on
                String list = "";
                for (int i = 0; i < agentArray.size(); i++) {
                    list = list + agentArray.get(i) + "\n";
                }
                //Set the list
                ((TextView) findViewById(R.id.proposedList)).setText(list);

                // Getting the vote counter and num players
                values = (HashMap<String, Integer>) snapshot.child("values").getValue();
                voteCounter = values.get("vote_counter");
                int numPlayers = values.get("num_players");
                proCounter = values.get("pro_counter");

                //check if vote counter reached num players
                if (voteCounter == numPlayers) {
                    //move on based off of whether or not it was approved
                    moveOn(proCounter > numPlayers / 2);
                }


            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });




    }

    public void handleApprove(View view) {
        //increment and update voteCounter and proCounter
        voteCounter++;
        proCounter++;
        values.put("vote_counter", voteCounter);
        values.put(("pro_counter"), proCounter);
        gameRef.child("values").setValue(values);
    }

    public void handleDeny(View view) {
        //increment and update voteCounter
        voteCounter++;
        values.put("vote_counter", voteCounter);
        gameRef.child("values").setValue(values);
    }


    public void moveOn(boolean accepted) {
        Intent toGoOrNotToGo;
        if (accepted) {
            //check if current player is an agent or not
            if (agentArray.contains(((Resistance)getApplication()).getPlayer())) {
                //go on mission if agent
                toGoOrNotToGo = new Intent(this, Mission.class);
            }
            else {
                //go to boring mission page if not
                toGoOrNotToGo = new Intent(this, Mission.class); // TODO: CHANGE TO BORING MISSION SCREEN
            }
        }
        else {
            //go back to proposal
            toGoOrNotToGo = new Intent(this, Proposal.class);
        }
        startActivity(toGoOrNotToGo);
    }
}
