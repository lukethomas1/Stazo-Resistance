package com.example.isaacwang.stazo_resistance;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MissionActiveActivity extends AppCompatActivity {

    private Firebase valuesRef;
    private String game_id;
    boolean keepDefault = true;
    private Long turnout; // DOES THIS HAVE TO BE LONG OR NAH
    private int failCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabotage);

        this.game_id = getIntent().getStringExtra("game_id");

        // Changes to Lobby handling
        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        valuesRef = fbRef.child("games").child(game_id).child("values");

        /*Resistance resistance = ((Resistance) this.getApplication());
        game = resistance.getGame();
        Player saboteur = game.getAgents().get(game.getSabotageIndex());
        ((TextView) findViewById(R.id.makeYourChoice)).setText(saboteur.getName() + ", make your choice!");*/

        // Set keepDefault to true or false with a 50/50 chance
        keepDefault = ((((int)(Math.random() * 100)) + 1) < 50);
        // If keepDefault is false, switch the text of the two buttons but not the functions
        if(!keepDefault) {
            TextView button1 = (TextView) findViewById(R.id.button1);
            TextView button2 = (TextView) findViewById(R.id.button2);
            button1.setText("Sabotage");
            button2.setText("Succeed");
        }
    }

    private void allVotesCounted() {
        Intent i = new Intent(this, MissionPassTho.class);
        i.putExtra("game_id", game_id);
        startActivity(i);
    }

    public void succeed(View view) {
        // Work as normal
        if(keepDefault) {
            proceed();
        }

        // Button has opposite text, so switch back to normal and call the other function
        else {
            keepDefault = true;
            fail(view);
        }
    }

    public void fail(View view) {
        // Work as normal
        if(keepDefault) {
            valuesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    failCount = ((Integer)snapshot.child("fail_counter").getValue()).intValue();
                    valuesRef.child("fail_counter").setValue(++failCount);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            proceed();
        }

        // Button has opposite text, so switch back to normal and call the other function
        else {
            keepDefault = true;
            succeed(view);
        }
    }

    // This method is called from both fail() and succeed() to increment the # of votes and continue
    // to the mission inactive screen to wait for the rest of the votes
    private void proceed() {
        //TODO increment # of votes for mission in firebase
        valuesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                turnout = ((Long) snapshot.child("sabotage_counter").getValue()).longValue();
                valuesRef.child("sabotage_counter").setValue(++turnout);
                goToInactive();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void goToInactive() {
        // After voting, send user to Inactive screen to wait for other voters
        Intent i = new Intent(this, MissionInactiveActivity.class);
        i.putExtra("game_id", game_id);
        startActivity(i);
        finish();
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
        i.putExtra("game_id", game_id);
        startActivity(i);
    }
}
