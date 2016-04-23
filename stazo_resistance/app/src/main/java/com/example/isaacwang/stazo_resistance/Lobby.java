package com.example.isaacwang.stazo_resistance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ansel on 4/7/16.
 */
public class Lobby extends AppCompatActivity {
    private String game_id;
    private Firebase gameRef;
    //private Firebase playerRef;
    private ArrayList<Player> playerArray;
    private int numPlayers;
    private Player thisPlayer;

    // Sequences for missions depending on number of players
    private static final Mission[] fiveSequence = {new Mission(2, 1), new Mission(3, 1),
            new Mission(2, 1), new Mission(3, 1), new Mission(3, 1)};
    private static final Mission[] sixSequence = {new Mission(2, 1), new Mission(3, 1),
            new Mission(4, 1), new Mission(3, 1), new Mission(4, 1)};
    private static final Mission[] sevenSequence = {new Mission(2, 1), new Mission(3, 1),
            new Mission(3, 1), new Mission(4, 2), new Mission(4, 1)};
    private static final Mission[] entSequence = {new Mission(3, 1), new Mission(4, 1),
            new Mission(4, 1), new Mission(5, 2), new Mission(5, 1)};

    // Array of all the sequences
    private static final Mission[][] allSequences = {fiveSequence, sixSequence, sevenSequence,
            entSequence, entSequence, entSequence};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.lobby);

        this.game_id = getIntent().getStringExtra("game_id");
        ((TextView) findViewById(R.id.idTextView)).setText(game_id);

        // Changes to Lobby handling
        Firebase fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
        gameRef = fbRef.child("games").child(game_id);
        Firebase playerRef = gameRef.child("players");
        Firebase readyRef = gameRef.child("values").child("proceed_to_proposal");

        // Get this player
        thisPlayer = getThisPlayer();

        // Adding players to the player array
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                clearGrid();

                // Copying the player array
                playerArray = ((ArrayList<Player>)
                        snapshot.getValue(new GenericTypeIndicator<List<Player>>() {
                        }));

                if (playerArray != null) {
                    // updating number of players
                    numPlayers = playerArray.size();

                    // displaying the names
                    for (Player p : playerArray) {
                        boolean isThisPlayer = thisPlayer.getNum() == p.getNum();

                        addPlayerToGrid(p.getName(), isThisPlayer);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        readyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (((Integer) dataSnapshot.getValue(Integer.class)).intValue() == 1) {
                    goToProposal();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Inform the player if he is the game creator that he can remove players
        if (thisPlayer.getNum() == 1) // If this player is the game creator
        {
            Context context = getApplicationContext();

            // Text to display
            CharSequence text = "Tap the yellow player to change your name.";

            // How long to display the toast
            int duration = Toast.LENGTH_LONG;

            // Display toast
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    public void addPlayerToGrid(String playerName, boolean isThisPlayer) {
        /* no duplicate additions
        if (gridContainsPlayer(playerName)) {
            return;
        } */
        // Get the grid
        LinearLayout grid = (LinearLayout) findViewById(R.id.player_container);

        // Create the EditText
        EditText playerET = new EditText(this);

        // Only one line of text
        playerET.setSingleLine(true);
        playerET.setFocusableInTouchMode(true);

        // Set the text
        playerET.setText(playerName);

        // Set background and text color
        if (isThisPlayer) {
            playerET.setBackground(getResources().getDrawable(R.drawable.lobby_this_player_button,
                    null));
            playerET.setTextColor(Color.parseColor("#878787"));
        } else {
            playerET.setBackground(getResources().getDrawable(R.drawable.lobby_button, null));
            playerET.setTextColor(Color.WHITE);

            // Prevent edit text from being editable if it is not this player
            playerET.setInputType(InputType.TYPE_NULL);
        }

        // Make button width a function of display width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int buttonWidth = 2 * metrics.widthPixels / 5; // 1/3 of the screen width
        int buttonHeight = metrics.heightPixels / 14; // 1/12 of the screen height

        // Set height and width
        playerET.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
        playerET.setGravity(Gravity.CENTER);

        playerET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText playerET = (EditText) v;

                // Clear the player name when the user taps on the ET
                if (thisPlayer.getName().equals(playerET.getText().toString())) {
                    playerET.setText("");
                }
            }
        });

        playerET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText playerET = (EditText) v;

                // Clear the player name when the user taps on the ET
                if (hasFocus && thisPlayer.getName().equals(playerET.getText().toString())) {
                    playerET.setText("");
                }
            }
        });

        playerET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                // Cast the calling view to an EditTExt
                EditText playerET = (EditText) v;

                // When the keyboard check/enter button is pressed
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT ) {

                    // Extract the current text from the edittext
                    String name = playerET.getText().toString();

                    // Hide the keyboard
                    hideKeyboard();

                    // Remove focus from the edittext, which clears the text
                    playerET.clearFocus();

                    // Set the text to the saved text before removing focus
                    playerET.setText( name );

                    // Update player name in Resistance
                    thisPlayer.setName( name );

                    // Update player name on database
                    int playerIndex = thisPlayer.getNum() - 1;

                    gameRef.child("players")
                            .child(Integer.toString(playerIndex))
                            .child("name").setValue(name);

                }
                return handled;
            }
        });

        // Add view to the grid
        grid.addView(playerET);
    }

    // Get the player on this phone
    public Player getThisPlayer() {
        Resistance resistance = (Resistance) getApplication();

        return resistance.getPlayer();
    }


    // is this name already in the grid?
    public boolean gridContainsPlayer(String name) {
        // Get the grid
        LinearLayout grid = (LinearLayout) findViewById(R.id.player_container);

        for (int i = 0; i < grid.getChildCount(); i++) {
            // Get the view at the current index
            EditText playerET = (EditText) grid.getChildAt(i);

            // Check if the player name is equal to the one to remove
            if (playerET.getText().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void removePlayerFromGrid(String playerName) {
        // Get the grid
        LinearLayout grid = (LinearLayout) findViewById(R.id.player_container);

        for (int i = 0; i < grid.getChildCount(); i++) {
            // Get the view at the current index
            EditText playerET = (EditText) grid.getChildAt(i);

            // Check if the player name is equal to the one to remove
            if (playerET.getText().equals(playerName)) {
                //removePlayerFromDatabase();

                // Remove the button from the grid
                grid.removeViewAt(i);

                break;
            }
        }
    }

    public void clearGrid() {
        LinearLayout grid = (LinearLayout) findViewById(R.id.player_container);

        grid.removeAllViews();
    }

    public void removePlayerFromDatabase() {
        Player thisPlayer = getThisPlayer();

        // Firebase stores the indices at 0
        int playerIndex = thisPlayer.getNum() - 1;

        // Remove the player from the firebase
        gameRef.child("players").child(Integer.toString(playerIndex)).removeValue();
    }


    // initializes game values
    public void startGame(View view) {

        if (numPlayers >= 5 && numPlayers <= 10) {

            // the sequence of missions stored on database
            gameRef.child("sequence").setValue(getMissionSequence());

            // we are ready for the game to start -> chain into goToProposal
            gameRef.child("values").child("proceed_to_proposal").setValue(new Integer(1));

            gameRef.child("values").child("num_players").setValue(new Integer(numPlayers));

            // assigns spies to the local playerArray
            setSpies();
            // push the spy-updates playerArray to the database
            gameRef.child("players").setValue(playerArray);
        }
        else {
            incorrectNumPlayers();
        }
    }

    // assigns spies to the local playerArray
    public void setSpies() {
        int maxSpies = (numPlayers + 2) / 3; //Gets max number of spies according to playerCount
        int numSpies = 0; //Current number of spies in array

        while (numSpies < maxSpies) {

            //Randomly generates index from 0 to playerCount-1
            int randomNum = (int) (Math.random() * numPlayers);

            //If the player at that index is not already a spy, make them a spy
            if (!playerArray.get(randomNum).isSpy()) {
                playerArray.get(randomNum).setSpy(true);
                numSpies++;
            }
        }
    }

    // called when ready is set to 1;
    public void goToProposal() {
        Intent i;
        if (((Resistance) getApplication()).getPlayer().getNum() == 1) {
            i = new Intent(this, Proposal.class);
        }
        else {
            i = new Intent(this, ProposalInactive.class);
        }
        i.putExtra("game_id", game_id);
        startActivity(i);
    }

    public void goToRoleReveal() {
        Intent i = new Intent(this, RoleRevealActivity.class);
        i.putExtra("game_id", game_id);
        startActivity(i);
    }

    public Mission[] getMissionSequence() {
        return allSequences[numPlayers-5];
    }

    public void incorrectNumPlayers() {
        String[] quitArray = {"Okay"};
        AlertDialog.Builder numEntry = new AlertDialog.Builder(this);

        if (numPlayers == 1) {
            numEntry.setTitle("You only have " + numPlayers + " player. Please try again when you have 5 to 10 players!");
        }
        else {
            numEntry.setTitle("You only have " + numPlayers + " players. Please try again when you have 5 to 10 players!");
        }
        numEntry.setItems(quitArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        numEntry.create();
        numEntry.show();
    }

    /*@Override
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
    }*/

    public void goToIntro() {
        Intent i = new Intent(this, IntroActivity.class);
        startActivity(i);
    }

}
