package com.example.Stopi.fragments;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.SensorManager.DATA_X;
import static android.hardware.SensorManager.DATA_Y;
import static android.hardware.SensorManager.SENSOR_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_GAME;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.Stopi.R;
import com.example.Stopi.objects.BallModel;

public class DistractionFragment extends Fragment implements Callback, SensorListener {

    private View view;

    private static final int BALL_RADIUS = 40;
    private final BallModel model = new BallModel(BALL_RADIUS);

    private SurfaceView surface;
    private SurfaceHolder holder;
    private GameLoop gameLoop;

    private Paint backgroundPaint;
    private Paint ballPaint;
    private Paint linePaint;
    private Paint holePaint;

    private SensorManager sensorMgr;
    private long lastSensorUpdate = -1;

    //=========================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_distract, container, false);

        surface = view.findViewById(R.id.bouncing_ball_surface);
        holder = surface.getHolder();
        holder.addCallback(this);
        initColors();

        return view;
    }

    public void initColors(){
        backgroundPaint = createPaint(Color.WHITE);
        ballPaint = createPaint(Color.RED);
        holePaint = createPaint(Color.GRAY);

        linePaint = createPaint(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(90/getResources().getDisplayMetrics().density);
    }

    public Paint createPaint(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    //=========================================

    @Override
    public void onPause() {
        super.onPause();

        model.setVibrator(null);
        sensorMgr.unregisterListener(this, SENSOR_ACCELEROMETER);
        sensorMgr = null;
        model.setAccel(0, 0);
    }

    //=========================================

    @Override
    public void onResume() {
        super.onResume();

        sensorMgr = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,
                SENSOR_ACCELEROMETER,
                SENSOR_DELAY_GAME);
        if (!accelSupported) {
            sensorMgr.unregisterListener(this, SENSOR_ACCELEROMETER);
            // TODO show an error
        }

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Activity.VIBRATOR_SERVICE);
        model.setVibrator(vibrator);
    }

    //===========================================

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        model.setSize(width, height);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop = new GameLoop();
        gameLoop.start();
    }

    private void draw() {
        Canvas c = null;
        try {
            c = holder.lockCanvas();
            if (c != null)
                doDraw(c);
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    private void doDraw(Canvas c) {
        int width = c.getWidth();
        int height = c.getHeight();
        c.drawRect(0, 0, width, height, backgroundPaint);

        float ballX, ballY;
        synchronized (model.LOCK) {
            ballX = model.ballPixelX;
            ballY = model.ballPixelY;
        }
        createMaze(c, ballX, ballY);
    }

    private void createMaze(Canvas c, float ballX, float ballY) {
        c.drawCircle(ballX, ballY, BALL_RADIUS, ballPaint);
        c.drawLine(0, 650, 700, 650, linePaint);
        c.drawLine(450, 1250, 1200, 1250, linePaint);
        c.drawCircle(550,1600,50,holePaint);
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            model.setSize(0,0);
            gameLoop.safeStop();
        } finally {
            gameLoop = null;
        }
    }

    //=========================================== call backs

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) { }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();

            if (lastSensorUpdate == -1 || (curTime - lastSensorUpdate) > 50) {
                lastSensorUpdate = curTime;

                model.setAccel(values[DATA_X], values[DATA_Y]);
            }
        }
    }

    //====================================================

    class GameLoop extends Thread {

        private volatile boolean running = true;
        public void run() {
            while (running) {
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                    draw();
                    model.updatePhysics();
                } catch (InterruptedException ie) {
                    running = false;
                }
            }
        }

        public void safeStop() {
            running = false;
            interrupt();
        }
    }
}

