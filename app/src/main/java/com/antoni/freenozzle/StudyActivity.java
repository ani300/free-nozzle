package com.antoni.freenozzle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;


public class StudyActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    int mmorDtchoose = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        // Get info from intent
        final double mP0 = getIntent().getDoubleExtra("P0", 0.0);
        final double mT0 = getIntent().getDoubleExtra("T0", 0.0);
        final double mR = getIntent().getDoubleExtra("r", 0.0);
        final double mGamma = getIntent().getDoubleExtra("gamma", 0.0);

        TextView mInfo = (TextView) findViewById(R.id.textView11);
        DecimalFormat df = new DecimalFormat("#.##");
        double Pstar = mP0*Math.pow(2.0 / (mGamma + 1), mGamma / (mGamma - 1));
        mInfo.setText(mInfo.getText() + " " + df.format(Pstar) + ".");

        Spinner mSpinner = (Spinner) findViewById(R.id.spinner2);
        mSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mord_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        Button mButton = (Button) findViewById(R.id.button2);
        final EditText mmorDt = (EditText) findViewById(R.id.editText5);
        final EditText mPe = (EditText) findViewById(R.id.editText7);
        final EditText mPa = (EditText) findViewById(R.id.editText6);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(StudyActivity.this, StudyResultActivity.class);

                // Put all needed variables in Intent
                intent.putExtra("P0", mP0);
                intent.putExtra("T0", mT0);
                intent.putExtra("r", mR);
                intent.putExtra("gamma", mGamma);
                intent.putExtra("Pe", Double.parseDouble(mPe.getText().toString()));
                intent.putExtra("Pa", Double.parseDouble(mPa.getText().toString()));
                if (mmorDtchoose == 0) {
                    intent.putExtra("mmorDt", 0);
                    intent.putExtra("m", Double.parseDouble(mmorDt.getText().toString()));
                }
                else if(mmorDtchoose == 1) {
                    intent.putExtra("mmorDt", 1);
                    intent.putExtra("Dt", Double.parseDouble(mmorDt.getText().toString()));
                }
                startActivity(intent);
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String s = (String) parent.getItemAtPosition(pos);
        if (s.equals("m (kg/s)")) {
            mmorDtchoose = 0;
        }
        else if (s.equals("Dt (m)")) {
            mmorDtchoose = 1;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
