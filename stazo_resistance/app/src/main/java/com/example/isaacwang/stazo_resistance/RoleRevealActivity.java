package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RoleRevealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rolereveal);

        // Get the intent that created this activity
        Intent intent = getIntent();
        // Get the text field that displays the role of the player
        TextView roleText = (TextView)findViewById(R.id.role_textView);
        // Set the text to the player's role
        Player currentPlayer = ((Resistance)getApplication()).getGame().getNextPlayer();
        // Make sure player isn't null
        if(currentPlayer != null) {
            roleText.setText("Testing");
            // If player is a spy, change text to say "Spy" rather than "Rebel"
            if(currentPlayer.isSpy()) {
                roleText.setText("Spy");
            }
        }
    }

    public void goToName(View view){
        Intent i = new Intent(this, NameEntry.class);
        startActivity(i);
    };
}
