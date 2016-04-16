package com.example.isaacwang.stazo_resistance;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_inactive);
        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        gameRef = fbRef.child("games").child(game_id);
        grabData();

        ((TextView)findViewById(R.id.proposing)).setText(playerName + " is proposing...");
    }

    private void grabData() {
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                vals = ((HashMap<String, Integer>)
                        snapshot.child("values").getValue(
                                new GenericTypeIndicator<HashMap<String, Integer>>() {
                                }));

                String proposer_index = ((Integer) vals.get("proposer_index")).toString();

                // grabbing the proposingplayer
                playerName = ((Player)
                        snapshot.child("players").child(proposer_index).getValue()).getName();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (vals.containsKey("proceed_to_vote") &&
                        vals.get("proceed_to_vote") == 1) {
                    proceedToVote();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void proceedToVote(){}
}
