package com.example.isaacwang.stazo_resistance;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Sabotage extends AppCompatActivity {

    private Game game;
    boolean keepDefault = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int FULL = 100;
        final int HALF = 50;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabotage);

        Resistance resistance = ((Resistance) this.getApplication());
        game = resistance.getGame();
        Player saboteur = game.getAgents().get(game.getSabotageIndex());
        ((TextView) findViewById(R.id.makeYourChoice)).setText(saboteur.getName() + ", make your choice!");

        // Randomly place the succeed and fail buttons
        keepDefault = ((((int)(Math.random() * FULL)) + 1) < HALF);
        if(!keepDefault) {
            TextView button1 = (TextView) findViewById(R.id.button);
            TextView button2 = (TextView) findViewById(R.id.button2);
            button1.setText("Fail");
            button2.setText("Succeed");
        }
    }

    public void succeed(View view) {
        if(keepDefault) {
            proceed();
        }

        else {
            keepDefault = true;
            fail(view);
        }
    }

    public void fail(View view) {
        if(keepDefault) {
            game.sabotageMission();
            proceed();
        }

        else {
            keepDefault = true;
            succeed(view);
        }
    }

    private void proceed() {
        game.incrementSabotager();
        Intent i;

        //If all agents have succeeded/sabotaged
        if (game.getSabotageIndex() >= game.getAgents().size()) {
            i = new Intent(this, MissionPassTho.class);
            startActivity(i);
        }
        else {
            i = new Intent(this, Sabotage.class);
            startActivity(i);
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
}
