package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ericzhang on 3/29/16.
 */
public class MissionPassTho extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_pass_tho);
    }

    public void handleClick(View view)
    {
        //increment resistance points
        Game game = new Game(7);
        game.incrementResistanceScore();

        //Just move on to next proposal if resistance points < 3
        if (game.getResistanceScore() <3) { //heart tho
//            Intent intent = new Intent(this, Proposal.class);
//            startActivity(intent);
        }
        //otherwise gg resistance op, spies suck
        else {
            //Intent intent = new Intent(this, GameOver.class);
            //startActivity(intent);
        }
    }
}
