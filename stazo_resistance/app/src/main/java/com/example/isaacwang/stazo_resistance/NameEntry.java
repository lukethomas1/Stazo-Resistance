package com.example.isaacwang.stazo_resistance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NameEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entername);
    }

    public void handleClick(View view)
    {
        // Check if a String was entered
        EditText nameField = (EditText) findViewById(R.id.nameEditText);

        // Check for user clicking done with no name

        if (nameField.getText().toString().equals(""))
        {
            EnterNameFragment dialogFragment = new EnterNameFragment();

            // Show the alert prompting the user to enter a name
            dialogFragment.show(getFragmentManager(), "AlertDialog");
        }

        // Switch screens to role reveal
        else
        {
            // Transfer control to RoleRevealActivity
            Intent intent = new Intent(this, RoleRevealActivity.class);
            startActivity(intent);
        }
    }

    public class EnterNameFragment extends DialogFragment
    {
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Build dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Please enter a name") // Set the message
                    // Set the button to let the user close
                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Let the user close the dialog without doing anything
                        }
                    });

            // Return the constructed AlertDialog
            return builder.create();
        }
    }




}
