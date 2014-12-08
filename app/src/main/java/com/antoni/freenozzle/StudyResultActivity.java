package com.antoni.freenozzle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;


public class StudyResultActivity extends ActionBarActivity {

    double P0;
    double T0;
    double r;
    double gamma;
    double Pe;
    double Pa;
    int morDt;
    double m;
    double Dt;

    double De;
    double F;
    double ve;
    double At;
    double Ae;
    double Me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_result);

        // Get info form the intent to perform all calculations
        P0 = getIntent().getDoubleExtra("P0", 0.0);
        T0 = getIntent().getDoubleExtra("T0", 0.0);
        r = getIntent().getDoubleExtra("r", 0.0);
        gamma = getIntent().getDoubleExtra("gamma", 0.0);
        Pe = getIntent().getDoubleExtra("Pe", 0.0);
        Pa = getIntent().getDoubleExtra("Pa", 0.0);
        morDt = getIntent().getIntExtra("mmorDt", 0);
        if (morDt == 0) {
            m = getIntent().getDoubleExtra("m", 0.0);
        }
        else {
            Dt = getIntent().getDoubleExtra("Dt", 0.0);
        }

        // Perform all calculations
        performCalculus();

        // Present results
        DecimalFormat df = new DecimalFormat("#.####");
        TextView mThrust = (TextView) findViewById(R.id.textView13);
        TextView mDt = (TextView) findViewById(R.id.textView14);
        TextView mDe = (TextView) findViewById(R.id.textView15);

        mThrust.setText(mThrust.getText() + " " + df.format(F));
        mDt.setText(mDt.getText() + " " + df.format(Dt));
        mDe.setText(mDe.getText() + " " + df.format(De));

    }

    @Override
    protected void onResume() {
        super.onResume();
        NozzleView mPaint = (NozzleView) findViewById(R.id.paintView);
        NozzleView.NozzleThread mThread = mPaint.getThread();
        mThread.provideData(Dt, De);
    }

    private void performCalculus() {
        // 1. Calculate m_dot or Dt as needed, using Pt, Tt as auxiliaries
        double Pt = P0*Math.pow(2.0 / (gamma + 1), gamma / (gamma - 1)); // bar
        double Tt = T0*(2/(gamma + 1)); // K
        // m_dot = rho_t * A_t * v_t = (P_t * A_t * sqrt(gamma))/sqrt(r * T_t)
        if (morDt == 0) {
            Dt = Math.sqrt((4 * m * Math.sqrt(r * Tt)) / (Math.PI * Pt * 10000 * Math.sqrt(gamma))); // m
            At = Math.PI * Math.pow(Dt, 2) / 4; // m2
        }
        else if(morDt == 1) {
            At = Math.PI * Math.pow(Dt, 2) / 4; // m2
            m = (Pt*10000*At*Math.sqrt(gamma))/Math.sqrt(r*Tt); // kg/s
        }
        ve = Math.sqrt((2 * gamma * r * T0) / (gamma - 1) * (1 - Math.pow(Pe / P0, (gamma - 1) / gamma))); // m/s
        Me = Math.sqrt(2.0/(gamma-1)*(Math.pow(P0/Pe, (gamma-1)/gamma)-1));
        Ae = At* Math.sqrt(1 / (Me * Me) * Math.pow(2 / (gamma + 1) * (1 + (gamma - 1) / 2 * Me * Me), (gamma + 1) / (gamma - 1))); // m2
        De = Math.sqrt(4*Ae/Math.PI); // m
        F = m*ve + Ae*(Pe-Pa)*10000; // N

        Log.v("WOLOLO", Double.toString(P0));
        Log.v("WOLOLO", Double.toString(T0));
        Log.v("WOLOLO", Double.toString(Pt));
        Log.v("WOLOLO", Double.toString(Tt));
        Log.v("WOLOLO", Double.toString(r));
        Log.v("WOLOLO", Double.toString(gamma));
        Log.v("WOLOLO", Double.toString(Pe));
        Log.v("WOLOLO", Double.toString(Pa));
        Log.v("WOLOLO", Integer.toString(morDt));

        Log.v("WOLOLO", Double.toString(m));
        Log.v("WOLOLO", Double.toString(Dt));
        Log.v("WOLOLO", Double.toString(De));
        Log.v("WOLOLO", Double.toString(At));
        Log.v("WOLOLO", Double.toString(Ae));
        Log.v("WOLOLO", Double.toString(F));
        Log.v("WOLOLO", Double.toString(ve));
        Log.v("WOLOLO", Double.toString(Me));

    }
}
