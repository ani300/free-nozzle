package com.antoni.freenozzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    static final String mOperation = "OPERATION";
    static final int mDesign = 1;
    static final int mStudy = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mDesign = (Button) findViewById(R.id.design_button);
        Button mStudy = (Button) findViewById(R.id.study_button);

        mDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GasCombustionActivity.class);
                intent.putExtra(mOperation, MainActivity.mDesign);
                startActivity(intent);
            }
        });

        mStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GasCombustionActivity.class);
                intent.putExtra(mOperation, MainActivity.mStudy);
                startActivity(intent);
            }
        });

    }
}
