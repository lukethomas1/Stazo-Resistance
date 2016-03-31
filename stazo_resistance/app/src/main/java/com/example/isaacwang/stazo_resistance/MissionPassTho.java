package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ericzhang on 3/29/16.
 */
public class MissionPassTho extends AppCompatActivity{
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_pass_tho);
        Resistance resistance = ((Resistance) this.getApplication());
        game = resistance.getGame();

        //for updating the text message
        //if mission passes
        if (game.executeMission()) {
            ((TextView) findViewById(R.id.missionPassedText)).setText("Mission Passed! :D");

        }
        //if mission fails
        else {
            ((TextView) findViewById(R.id.missionPassedText)).setText("Mission Failed! D:");
        }
    }

    public void handleClick(View view)
    {
        //Resistance won
        if (game.getResistanceScore() == 3) {
            //update the fact that resistance won
            game.setWhoWon(true);
            Intent intent = new Intent(this, GameOver.class);
            startActivity(intent);
        }
        //Spies won
        else if (game.getSpyScore() == 3) {
            //update the fact that spies won
            game.setWhoWon(false);
            Intent intent = new Intent(this, GameOver.class);
            startActivity(intent);
        }
        //otherwise go back to proposal screen
        else {
            Intent intent = new Intent(this, Proposal.class);
            startActivity(intent);
        }
    }
}
