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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class IntroActivity extends AppCompatActivity {

    private String android_id;
    private Firebase fbRef;
    private Player me = new Player("lmaoooo");
    private String game_id;
    private ArrayList<Player> p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introscreen);
        game_id = generateGameId();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        fbRef =
                new Firebase(((Resistance) getApplication()).getFbURL());


    }

    /**
     * For when a player creates a new game
     * @param view
     */
    public void startGame(View view){
        // Creating the empty game

        // Name entry dialog
        AlertDialog.Builder nameEntry = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        nameEntry.setTitle("Enter your name");
        nameEntry.setView(input);
        nameEntry.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //me = new Player(input.getText().toString());
            }
        });
        nameEntry.show();

        final Firebase playerRef = fbRef.child("games").child(game_id).child("players");
        p = new ArrayList<Player>();
        p.add(me);
        playerRef.setValue(p);

        // Go to lobby screen
        Intent toLobby = new Intent( this, Lobby.class );

        startActivity( toLobby );

    }

    /**
     * Starts the name entry activity and initializes game
     * @param whichButton the button pressed in number of players popup
     */
    public void startNameEntry(int whichButton) {

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
