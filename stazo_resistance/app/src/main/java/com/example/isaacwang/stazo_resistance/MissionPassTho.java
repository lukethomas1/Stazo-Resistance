package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ericzhang on 3/29/16.
 */
public class MissionPassTho extends AppCompatActivity{
    private Firebase gameRef;
    private Firebase valsRef;
    private String game_id;
    private HashMap<String, Integer> vals;
    private boolean pass;
    private int res_score;
    private int spy_score;
    private int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_pass_tho);

        this.game_id = getIntent().getStringExtra("game_id");

        Firebase fbRef = new Firebase(((Resistance) getApplication()).getFbURL());

        gameRef = fbRef.child("games").child(game_id);
        valsRef = fbRef.child("games").child(game_id).child("values");

        grabData();

        valsRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if(((Long)(snapshot.child("proceed_from_MissionPassTho")).getValue()).longValue() == 1){
                    valsRef.removeEventListener(this);
                    proceed();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    // Ultimately, just sets pass to true or false
    public void grabData() {
        //SINGLE GRAB
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                vals = ((HashMap<String, Integer>)
                        snapshot.child("values").getValue(
                                new GenericTypeIndicator<HashMap<String, Integer>>() {
                                }));

                round = ((Integer) vals.get("round")).intValue();
                int fails = ((Integer) vals.get("fail_counter")).intValue();
                System.out.println("fail counter is " + fails);
                res_score = ((Integer) vals.get("res_score")).intValue();
                spy_score = ((Integer) vals.get("spy_score")).intValue();
                ArrayList<Mission> sequence = ((ArrayList<Mission>) snapshot.child("sequence").getValue(
                        new GenericTypeIndicator<List<Mission>>() {
                        }));
                pass = (sequence.get(round).getFails() > fails);

                if (pass) {
                    ((TextView) findViewById(R.id.missionPassedText)).setText("Mission Passed! :D");
                    res_score++;
                }
                //if mission fails
                else {
                    ((TextView) findViewById(R.id.missionPassedText)).setText("Mission Failed! D:");
                    spy_score++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void handleClick(View view) {
        valsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                valsRef.child("proceed_from_MissionPassTho").setValue(1);
                valsRef.removeEventListener(this);
                proceed();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void proceed() {
        Intent intent;
        //Resistance won
        if (res_score == 3) {
            //update the fact that resistance won
            intent = new Intent(this, GameOver.class);
            intent.putExtra("winner", "Resistance");
        }
        //Spies won
        else if (spy_score == 3) {
            //update the fact that spies won
            intent = new Intent(this, GameOver.class);
            intent.putExtra("winner", "Spies");
        }
        //otherwise go back to proposal screen
        else {
            // kinda jank, only the creator updates score and round
            if (((Resistance) getApplication()).getPlayer().getNum() == 1) {
                System.out.println("you are updating this shit");
                vals.put("res_score", new Integer(res_score));
                vals.put("spy_score", new Integer(spy_score));
                vals.put("round", new Integer(round + 1));
                gameRef.child("values").setValue(vals);
            }
            if (((Resistance) getApplication()).getPlayer().getNum() ==
                    vals.get("proposer_index") + 1) {
                intent = new Intent(this, Proposal.class);
            }
            else {
                intent = new Intent(this, ProposalInactive.class);
            }
        }

        intent.putExtra("game_id", game_id);
        startActivity(intent);
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
