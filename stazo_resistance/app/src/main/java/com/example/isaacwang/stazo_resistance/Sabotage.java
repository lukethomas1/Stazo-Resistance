package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Sabotage extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabotage);

        Resistance resistance = ((Resistance) this.getApplication());
        game = resistance.getGame();
        Player saboteur = game.getAgents()[game.getSabotageIndex()];
        ((TextView) findViewById(R.id.makeYourChoice)).setText(saboteur.getName() + ", make your choice!");
    }

    public void succeed(View view) {
        proceed();
    }

    public void fail(View view) {
        game.sabotageMission();
        proceed();
    }

    private void proceed() {
        game.incrementSabotager();
        Intent i;

        //If all agents have succeeded/sabotaged
        if (game.getSabotageIndex() >= game.getAgentIndex()) {
            i = new Intent(this, MissionPassTho.class);
            startActivity(i);
        }
        else {
            i = new Intent(this, Sabotage.class);
            startActivity(i);
        }
    }






}
