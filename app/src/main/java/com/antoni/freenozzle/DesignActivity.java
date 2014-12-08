package com.antoni.freenozzle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class DesignActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        // Get info from intent
        final double mP0 = getIntent().getDoubleExtra("P0", 0.0);
        final double mT0 = getIntent().getDoubleExtra("T0", 0.0);
        final double mR = getIntent().getDoubleExtra("r", 0.0);
        final double mGamma = getIntent().getDoubleExtra("gamma", 0.0);

        Button mButton = (Button) findViewById(R.id.button3);
        final EditText mF = (EditText) findViewById(R.id.editText8);
        final EditText mPe = (EditText) findViewById(R.id.editText9);
        final EditText mPa = (EditText) findViewById(R.id.editText10);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DesignActivity.this, DesignResultActivity.class);

                // Put all needed variables in Intent
                intent.putExtra("P0", mP0);
                intent.putExtra("T0", mT0);
                intent.putExtra("r", mR);
                intent.putExtra("gamma", mGamma);
                intent.putExtra("Pe", Double.parseDouble(mPe.getText().toString()));
                intent.putExtra("Pa", Double.parseDouble(mPa.getText().toString()));
                intent.putExtra("F", Double.parseDouble(mF.getText().toString()));
                startActivity(intent);
            }
        });
    }

}
