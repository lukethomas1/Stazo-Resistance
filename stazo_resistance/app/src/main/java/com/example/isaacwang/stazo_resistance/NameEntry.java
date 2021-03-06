package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class NameEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.entername);
        //setPlayerNumberText();

        Firebase fbRef = new Firebase(((Resistance)getApplication()).getFbURL());
        /*Firebase userRef = fbRef.child("players").child("players1");
        userRef.setValue(new Player(5));*/
    }

    /*public void handleClick(View view)
    {
        // Check if a String was entered
        EditText nameField = (EditText) findViewById(R.id.nameEditText);

        // The name retrieved from the EditText
        String name = nameField.getText().toString();

        // Check for user clicking done with no name

        if (name.equals(""))
        {
            promptNameEntry();
        }

        // Switch screens to role reveal
        else
        {
            // Save the name data in the player objects
            storeNameData(name);

            // Transfer control to RoleRevealActivity
            Intent intent = new Intent(this, RoleRevealActivity.class);
            startActivity(intent);
        }
    }

    private void setPlayerNumberText()
    {
        // Get Player Number TextView
        TextView playerTextView = (TextView) findViewById(R.id.playerNumTextView);

        // Set the player number.  Will be 1+ the current iterator index
        playerTextView.setText(playerTextView.getText() + " " + (getGame().getIteratorIndex() + 1));
    }


    private void storeNameData(String name)
    {
        // Retrieve the game
        Game game = getGame();

        // Add the player name
        game.addPlayerName(name);
    }

    private Game getGame()
    {
        // Retrieve the Resistance application
        Resistance resistance = (Resistance) getApplication();

        // Retrieve the game
        Game game = resistance.getGame();

        return game;
    }


    private void promptNameEntry()
    {
        // Create DialogFragment to manage AlertDialog
        EnterNameFragment dialogFragment = new EnterNameFragment();

        // Show the alert prompting the user to enter a name
        dialogFragment.show(getFragmentManager(), "AlertDialog");
    }


    public class EnterNameFragment extends DialogFragment
    {
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Build dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Please enter a name") // Set the message
                    // Set the button to let the user close
                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Let the user close the dialog without doing anything
                        }
                    });

            // Return the constructed AlertDialog
            return builder.create();
        }
    }

//BACK LOGIC----------------------------------------------------------------------------------------------------------------------
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
    }*/
}
