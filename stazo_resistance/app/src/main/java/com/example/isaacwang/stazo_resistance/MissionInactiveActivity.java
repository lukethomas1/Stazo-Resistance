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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_inactive);

        final Firebase voteCount;

        fbRef = new Firebase(((Resistance) getApplication()).getFbURL());
        voteCount = fbRef.child(getIntent().getStringExtra("game_id")).child("values").child("voter_turnout");

        // This will constantly update the # of people that have voted
        voteCount.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                // Get the text box for vote counting and update it each time the # of votes increases
                ((TextView)findViewById(R.id.votecounter)).setText(snapshot.getValue().toString());

                // Check if everybody has voted
                if(((Integer)snapshot.getValue()).intValue() == 5) {
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
        startActivity(i);
    }
}
