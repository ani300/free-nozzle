package com.antoni.freenozzle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;


public class DesignResultActivity extends ActionBarActivity {

    double P0;
    double T0;
    double r;
    double gamma;
    double Pe;
    double Pa;
    double F;

    double De;
    double Dt;
    double At;
    double Ae;
    double Me;
    double ve;
    double m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_result);

        // Get info form the intent to perform all calculations
        P0 = getIntent().getDoubleExtra("P0", 0.0);
        T0 = getIntent().getDoubleExtra("T0", 0.0);
        r = getIntent().getDoubleExtra("r", 0.0);
        gamma = getIntent().getDoubleExtra("gamma", 0.0);
        Pe = getIntent().getDoubleExtra("Pe", 0.0);
        Pa = getIntent().getDoubleExtra("Pa", 0.0);
        F = getIntent().getDoubleExtra("F", 0.0);

        // Perform all calculations
        performCalculus();

        // Present results
        DecimalFormat df = new DecimalFormat("#.####");
        TextView mM = (TextView) findViewById(R.id.textView22);
        TextView mDt = (TextView) findViewById(R.id.textView20);
        TextView mDe = (TextView) findViewById(R.id.textView21);

        mM.setText(mM.getText() + " " + df.format(m));
        mDt.setText(mDt.getText() + " " + df.format(Dt));
        mDe.setText(mDe.getText() + " " + df.format(De));
    }

    private void performCalculus() {
        // 1. Calculate m_dot or Dt as needed, using Pt, Tt as auxiliaries
        double Pt = P0*Math.pow(2.0 / (gamma + 1), gamma / (gamma - 1)); // bar
        double Tt = T0*(2/(gamma + 1)); // K

        ve = Math.sqrt((2 * gamma * r * T0) / (gamma - 1) * (1 - Math.pow(Pe / P0, (gamma - 1) / gamma))); // m/s
        double k = (Pt*Math.sqrt(gamma))/Math.sqrt(r * Tt);
        Me = Math.sqrt(2.0/(gamma-1)*(Math.pow(P0/Pe, (gamma-1)/gamma)-1));

        Ae = F / ((k*ve/Math.sqrt(1 / (Me * Me) * Math.pow(2 / (gamma + 1) * (1 + (gamma - 1) / 2 * Me * Me), (gamma + 1) / (gamma - 1)))) + (Pe-Pa)*10000);
        At = Ae / Math.sqrt(1 / (Me * Me) * Math.pow(2 / (gamma + 1) * (1 + (gamma - 1) / 2 * Me * Me), (gamma + 1) / (gamma - 1))); // m2

        De = Math.sqrt(4*Ae/Math.PI); // m
        Dt = Math.sqrt(4*At/Math.PI); // m

        m = k*At;

        Log.v("WOLOLO", Double.toString(P0));
        Log.v("WOLOLO", Double.toString(T0));
        Log.v("WOLOLO", Double.toString(r));
        Log.v("WOLOLO", Double.toString(gamma));
        Log.v("WOLOLO", Double.toString(Pe));
        Log.v("WOLOLO", Double.toString(Pa));
        Log.v("WOLOLO", Double.toString(F));

        Log.v("WOLOLO", Double.toString(m));
        Log.v("WOLOLO", Double.toString(Dt));
        Log.v("WOLOLO", Double.toString(De));
        Log.v("WOLOLO", Double.toString(At));
        Log.v("WOLOLO", Double.toString(Ae));
        Log.v("WOLOLO", Double.toString(ve));
        Log.v("WOLOLO", Double.toString(Me));

    }

    @Override
    protected void onResume() {
        super.onResume();
        NozzleView mPaint = (NozzleView) findViewById(R.id.view);
        NozzleView.NozzleThread mThread = mPaint.getThread();
        mThread.provideData(Dt, De);
    }

}
