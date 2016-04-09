package com.example.isaacwang.stazo_resistance;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ericzhang on 4/8/16.
 */
public class VoteMission extends AppCompatActivity{

    private String android_id;
    private Firebase gameRef;
    private Firebase agentRef;
    private ArrayList<Player> agentArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_mission);

        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());

        gameRef = fbRef.child("games").child("game_id");
        agentRef = gameRef.child("agents");


        // Single-execution for adding us to the player array
        agentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Copying the player array
                agentArray = ((ArrayList<Player>) snapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        String list = "";
        for (int i = 0; i < agentArray.size(); i++) {
            list = list + agentArray.get(i) + "\n";
        }
        ((TextView) findViewById(R.id.proposedList)).setText(list);

    }


}
