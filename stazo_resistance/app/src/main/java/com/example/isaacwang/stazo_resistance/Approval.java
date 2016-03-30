package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Approval extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        Resistance resistance = ((Resistance) this.getApplication());
        game = resistance.getGame();

        //Prep for the next proposer, whether the mission is approved or not
        game.incrementProposer();
    }

    public void approved (View view) {
        Intent intent = new Intent(this, Sabotage.class);
        startActivity(intent);
    }
    public void disapproved (View view) {
        Intent intent = new Intent(this, Proposal.class);
        startActivity(intent);
    }
}
