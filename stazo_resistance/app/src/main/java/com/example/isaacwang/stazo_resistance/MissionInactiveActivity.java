package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MissionInactiveActivity extends AppCompatActivity {
    private Firebase fbRef;
    private Firebase gameRef;
    private String game_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_inactive);

        final Firebase valsRef;
        fbRef = new Firebase(((Resistance) getApplication()).getFbURL());
        game_id = getIntent().getStringExtra("game_id");
        gameRef = fbRef.child("games").child(game_id);
        valsRef = fbRef.child("games").child(game_id).child("values");

        // This will constantly update the # of people that have voted
        gameRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                // Get the text box for vote counting and update it each time the # of votes increases
                ((TextView)findViewById(R.id.votecounter)).setText(
                        snapshot.child("values").child("sabotage_counter").getValue().toString());
                Long sabotage_counter = ((Long)
                        snapshot.child("values").child("sabotage_counter").getValue()).longValue();
                System.out.println("sabotage is " + sabotage_counter);
                Long round = ((Long)
                        snapshot.child("values").child("round").getValue()).longValue();
                System.out.println("round is " + round);
                int numMems = ((Mission)
                        snapshot.child("sequence").child(round.toString()).getValue(Mission.class)).getMems();
                System.out.println("num mems is " + numMems);
                // Check if everybody has voted
                if(sabotage_counter >= numMems) {
                    System.out.println("let's goooo");
                    gameRef.removeEventListener(this);
                    allVotesCounted();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void allVotesCounted() {
        Intent i = new Intent(this, MissionPassTho.class);
        i.putExtra("game_id", game_id);
        startActivity(i);
    }
}
