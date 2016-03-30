package com.example.isaacwang.stazo_resistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Approval extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
    }

    public void approved (View view) {
        Intent intent = new Intent(this, Sabotage.class);
        startActivity(intent);
    }
}
