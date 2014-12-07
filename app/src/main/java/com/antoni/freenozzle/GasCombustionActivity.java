package com.antoni.freenozzle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class GasCombustionActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private int mRorMMchoose = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_combustion);

        final int mOperation = getIntent().getIntExtra(MainActivity.mOperation, 0);

        // UI Operations
        final Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.rormm_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        Button mButton = (Button) findViewById(R.id.button);
        final EditText mPressure = (EditText) findViewById(R.id.editText);
        final EditText mTemperature = (EditText) findViewById(R.id.editText2);
        final EditText mRorMM = (EditText) findViewById(R.id.editText3);
        final EditText mGamma = (EditText) findViewById(R.id.editText4);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mOperation == 1) {
                    // Go to design
                    intent = new Intent(GasCombustionActivity.this, DesignActivity.class);
                }
                else {
                    // Go to study
                    intent = new Intent(GasCombustionActivity.this, StudyActivity.class);
                }
                // Put all needed variables in Intent
                intent.putExtra("P0", Double.parseDouble(mPressure.getText().toString()));
                intent.putExtra("T0", Double.parseDouble(mTemperature.getText().toString()));
                double r = 0.0;
                if (mRorMMchoose == 0) {
                    r = Double.parseDouble(mRorMM.getText().toString());
                }
                else if (mRorMMchoose == 1) {
                    r = 8.314/Double.parseDouble(mRorMM.getText().toString())*1000.0;
                }
                intent.putExtra("r", r);
                intent.putExtra("gamma", Double.parseDouble(mGamma.getText().toString()));
                startActivity(intent);
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String s = (String) parent.getItemAtPosition(pos);
        if (s.equals("r (J/(kg*K))")) {
            mRorMMchoose = 0;
        }
        else if (s.equals("MM (g/mol)")) {
            mRorMMchoose = 1;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
