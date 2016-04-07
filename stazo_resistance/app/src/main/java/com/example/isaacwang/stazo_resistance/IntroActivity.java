package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.Random;

public class IntroActivity extends AppCompatActivity {

    private String android_id;
    private Firebase fbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introscreen);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());
    }

    public void startGame(View view){
        AlertDialog.Builder numEntry = new AlertDialog.Builder(this);
        numEntry.setTitle("Enter the number of players");
        numEntry.setItems(R.array.numArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startNameEntry(whichButton);
            }
        });
        numEntry.create();
        numEntry.show();
    }

    /**
     * Starts the name entry activity and initializes game
     * @param whichButton the button pressed in number of players popup
     */
    public void startNameEntry(int whichButton) {
        String game_id = generateGameId();

        Game game = new Game(whichButton + 5);

        // Creating the game
        Firebase gameRef = fbRef.child("games").child(game_id);
        gameRef.setValue(new Game(whichButton + 5));

        /*((Resistance) this.getApplication()).
                setGame(new Game(whichButton + 5));*/
        Intent i = new Intent(this, NameEntry.class);
        startActivity(i);
    }

    public String generateGameId() {
        Random rand = new Random();
        int a;
        String code = "";
        for (int i=0; i < 6; i++ ) {
            a = rand.nextInt(10);
            code += a;
        }
        return code;
    }

    public Boolean hasPlayer(String id) {
        return true;
    }

    public void howToPlay (View view) {
        Intent intent = new Intent(this, HowToPlay.class);
        startActivity(intent);
    }
}
