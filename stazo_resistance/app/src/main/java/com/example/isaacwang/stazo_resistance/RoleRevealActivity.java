package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RoleRevealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rolereveal);

        // Get the intent that created this activity
        Intent intent = getIntent();
        // Get the text field that displays the role of the player
        TextView roleText = (TextView)findViewById(R.id.role_textView);
        // Set the text to the player's role
        Player currentPlayer = ((Resistance)getApplication()).getGame().getNextPlayer();
        // Make sure player isn't null
        if(currentPlayer != null) {
            // If player is a spy, change text to say "Spy" rather than "Rebel"
            if(currentPlayer.isSpy()) {
                roleText.setText("Spy");
            }
        }
    }

    /**
     * Called onClick when the OK button is pressed in RoleReveal, checks whether
     * to go on to mission proposal or back to name entry before starting the next
     * Activity.
     *
     * @param view
     */
    public void goToName(View view){
        // To be set to next Activity
        Intent i;
        // Get the global Game object
        Game gameObject = ((Resistance)getApplication()).getGame();

        // Check if this is the last player, if so go to mission proposal activity
        if(gameObject.getIteratorIndex() == gameObject.getNumPlayers()) {
            i = new Intent(this, Proposal.class);
        }

        // Not the last player, go back to name entry activity
        else {
            i = new Intent(this, NameEntry.class);
        }

        // Go to appropriate activity
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
