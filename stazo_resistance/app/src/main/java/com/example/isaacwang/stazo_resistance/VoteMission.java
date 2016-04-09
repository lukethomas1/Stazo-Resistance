package com.example.isaacwang.stazo_resistance;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericzhang on 4/8/16.
 */
public class VoteMission extends AppCompatActivity{

    private String android_id;
    private Firebase gameRef;
    private Firebase agentRef;
    private ArrayList<Player> agentArray;
    private String game_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_mission);

        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        gameRef = fbRef.child("games").child(game_id);
        agentRef = gameRef.child("agents");


        // Single-execution for adding us to the player array
        agentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // Copying the agent array
                agentArray = ((ArrayList<Player>) snapshot.getValue(
                        new GenericTypeIndicator<List<Player>>() {
                        }
                ));

                // Setting the String to empty
                String list = "";
                for (int i = 0; i < agentArray.size(); i++) {
                    list = list + agentArray.get(i).getName() + "\n";
                }
                ((TextView) findViewById(R.id.proposedList)).setText(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    public void approve(View view) {}
    public void deny(View view){}


}
