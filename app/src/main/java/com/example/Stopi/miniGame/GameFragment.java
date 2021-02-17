package com.example.Stopi.miniGame;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.KEYS;
import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.SensorManager.DATA_X;
import static android.hardware.SensorManager.DATA_Y;
import static android.hardware.SensorManager.SENSOR_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_GAME;

public class GameFragment extends Fragment implements SurfaceHolder.Callback, SensorListener {

    private View view;

    private final Ball ball = new Ball(KEYS.BALL_RADIUS);
    private Maze maze;

    private SurfaceView surface;
    private SurfaceHolder holder;
    private GameThread gameLoop;

    private SensorManager sensorMgr;
    private long lastSensorUpdate = -1;

    private TextView score;

    //=========================================

    @Override
    public void onStop() {
        super.onStop();
        if(DBreader.getInstance().getUser().setHighScore(gameLoop.getHolesScored())) {
            Utils.getInstance().createHighScoreDialog().show();
            DBupdater.getInstance().saveLoggedUser();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        sensorMgr.unregisterListener(this, SENSOR_ACCELEROMETER);
        sensorMgr = null;
        ball.setAccel(0, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMgr = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,
                SENSOR_ACCELEROMETER,
                SENSOR_DELAY_GAME);
        if (!accelSupported)
            sensorMgr.unregisterListener(this, SENSOR_ACCELEROMETER);
    }

    //=========================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view        = inflater.inflate(R.layout.fragment_distract, container, false);
        view        .setOnTouchListener(
                            (v, event) -> {
                                if(event.getAction() == MotionEvent.ACTION_UP)
                                    gameLoop.jumpBall();
                                return true;
                            });

        surface     = view.findViewById(R.id.bouncing_ball_surface);
        score       = view.findViewById(R.id.game_holes_scored);
        holder      = surface.getHolder();

        score       .setText(""+ 0);
        holder      .addCallback(this);

        maze        = new Maze(getContext());

        return      view;
    }

    //========================================= surface view call backs

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ball.setSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop = new GameThread(holder, ball, maze, score);
        gameLoop.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            ball.setSize(0,0);
            gameLoop.safeStop();
        } finally {
            gameLoop = null;
        }
    }

    //=========================================== sensors call backs

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) { }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();

            if (lastSensorUpdate == -1 || (curTime - lastSensorUpdate) > 50) {
                lastSensorUpdate = curTime;

                ball.setAccel(values[DATA_X], values[DATA_Y]);
            }
        }
    }

}
