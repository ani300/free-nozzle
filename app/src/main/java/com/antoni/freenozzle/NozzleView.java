package com.antoni.freenozzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Antoni on 07/12/2014.
 */
public class NozzleView extends SurfaceView implements SurfaceHolder.Callback {
    public class NozzleThread extends Thread {
        /*
        * Member (state) fields
        */
        private boolean mRun = false;
        /**
         * Current height of the surface/canvas.
         *
         * @see #setSurfaceSize
         */
        private int mCanvasHeight = 1;
        /**
         * Current width of the surface/canvas.
         *
         * @see #setSurfaceSize
         */
        private int mCanvasWidth = 1;

        private double Dt = 0.0;
        private double De = 0.0;
        private boolean dataProvided = false;

        // Coordinates of left throat circle center
        private double xLeftR;
        private double yLeftR;
        // Coordinates of left exit point
        private double xLeftDe;
        private double yLeftDe;

        private Paint mPaint;

        private void doDraw(Canvas canvas) {
            canvas.drawColor(0xffffffff);

            //Calculate scale
            double mScale = Math.min((double)mCanvasHeight / yLeftDe, (double)mCanvasWidth / Math.max(De, 2*Dt));
            canvas.drawArc(new RectF((float)((xLeftR-Dt/2)*mScale), 0, (float)((xLeftR+Dt/2)*mScale), (float)(Dt*mScale)), 270, 90+15, false, mPaint);
            canvas.drawArc(new RectF((float)((xLeftR+1.5*Dt)*mScale), 0, (float)((xLeftR+2.5*Dt)*mScale), (float)(Dt*mScale)), 270, -(90+15), false, mPaint);
            canvas.drawLine((float)((xLeftR+Dt/2*Math.cos(15*Math.PI/180))*mScale),(float)((yLeftR+Dt/2*Math.sin(15*Math.PI/180))*mScale),(float)(xLeftDe*mScale), (float)(yLeftDe*mScale), mPaint);
            canvas.drawLine((float)((xLeftR+2*Dt-Dt/2*Math.cos(15*Math.PI/180))*mScale),(float)((yLeftR+Dt/2*Math.sin(15*Math.PI/180))*mScale),(float)((xLeftDe+De)*mScale), (float)(yLeftDe*mScale), mPaint);
        }

        /** Handle to the surface manager object we interact with */
        private final SurfaceHolder mSurfaceHolder;

        public NozzleThread(SurfaceHolder surfaceHolder, Context context) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mContext = context;

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setARGB(255, 0, 0, 0);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth((float)2.0);
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        // Draw things
                        if (dataProvided) {
                            if (c != null) doDraw(c);
                        }
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         *
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(final int width, final int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
            }
        }

        public void provideData(double Dt, double De) {
            this.Dt = Dt;
            this.De = De;
            if (De / 2.0 > Dt) {
                xLeftDe = 0.0;
                xLeftR = De/2.0 - Dt;
            }
            else {
                xLeftR = 0.0;
                xLeftDe = Dt - De/2.0;
            }
            yLeftR = Dt/2;
            yLeftDe = yLeftR + (Dt/2.0)*Math.sin(15*Math.PI/180)+Math.abs(De/2 - Dt/2*(2-Math.cos(15*Math.PI/180)))/Math.tan(15*Math.PI/180);

            dataProvided = true;
        }
    }

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;
    /** The thread that actually draws the animation */
    private NozzleThread thread;
    public NozzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        // create thread only; it's started in surfaceCreated()
        thread = new NozzleThread(holder, context);
        setFocusable(true); // make sure we get key events
    }
    /**
     * Fetches the animation thread corresponding to this LunarView.
     *
     * @return the animation thread
     */
    public NozzleThread getThread() {
        return thread;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }
    /*
    * Callback invoked when the Surface has been created and is ready to be
    * used.
    */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();
    }
    /*
    * Callback invoked when the Surface has been destroyed and must no longer
    * be touched. WARNING: after this method returns, the Surface/Canvas must
    * never be touched again!
    */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}