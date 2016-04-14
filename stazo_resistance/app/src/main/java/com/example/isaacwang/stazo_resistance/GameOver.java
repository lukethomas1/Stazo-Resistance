package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    /*Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoverscreen);

        Resistance resistance = (Resistance) this.getApplication();
        game = resistance.getGame();
        boolean whoWon = game.getWhoWon();

        //if resistance won
        if(whoWon) {
            ((TextView) findViewById(R.id.wonText)).setText("Resistance wins!");
        }
        //if spies won
        else {
            ((TextView) findViewById(R.id.wonText)).setText("Spies win!");
        }
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

    public void returnToMain(View view) {
        Intent i = new Intent(this, IntroActivity.class);
        startActivity(i);
    }*/
}
