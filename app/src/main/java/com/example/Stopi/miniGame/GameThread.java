package com.example.Stopi.miniGame;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import com.example.Stopi.App;

import java.util.concurrent.TimeUnit;

public class GameThread extends Thread {

    private volatile boolean    running = true;
    private volatile long       lastTimeMs = -1;

    private SurfaceHolder       holder;
    private Ball                ball;
    private Maze                maze;

    private final float         pixelsPerMeter = 10;
    private final float         STOP_BOUNCING_VELOCITY = 2f;
    private float               lWidth, lHeight,lBallY, lBallX, lVy, lVx;

    private int                 ballRadius;
    private int                 holesScored;

    private TextView            score;

    //===========================================

    public GameThread(SurfaceHolder holder, Ball ball, Maze maze, TextView scoreTextView) {
        super();
        this.holder         = holder;
        this.ball           = ball;
        this.maze           = maze;
        this.score          = scoreTextView;
        ballRadius          = ball.getBallRadius();
        this.holesScored    = 0;
    }

    public void jumpBall() { ball.setVelocityY(ball.getVelocityY() - 50); }

    private void updateScore(){ this.score.post(() -> score.setText(""+ ++holesScored)); }

    public int getHolesScored() { return holesScored; }

    //===========================================

    public void run() {
        while (running) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
                draw();
                updatePhysics();
            } catch (InterruptedException ie) {
                running = false;
            }
        }
    }

    public void safeStop() {
        running = false;
        interrupt();
    }

    //===========================================

    private void draw() {
        Canvas c = null;
        try {
            c = holder.lockCanvas();
            if (c != null) doDraw(c);
        } finally {
            if (c != null) holder.unlockCanvasAndPost(c);
        }
    }

    private void doDraw(Canvas c) {
        int width = c.getWidth();
        int height = c.getHeight();
        c.drawRect(0, 0, width, height, maze.getBackgroundPaint());

        float ballX, ballY;
        synchronized (ball.getLOCK()) {
            ballX = ball.getBallPixelX();
            ballY = ball.getBallPixelY();
        }
        maze.createMaze(c, ballX, ballY);
    }

    //===========================================

    private void reactToHit(float lBall){
        lBallY = lBall;
        lVy = ball.setBouncedY(true);
    }

    public void updatePhysics() {
        float  lAx, lAy;
        synchronized (ball.getLOCK()) {
            lWidth          = ball.getPixelWidth();
            lHeight         = ball.getPixelHeight();
            lBallX          = ball.getBallPixelX();
            lBallY          = ball.getBallPixelY();
            lVx             = ball.getVelocityX();
            lVy             = ball.getVelocityY();
            lAx             = ball.getAccelX();
            lAy             = -ball.getAccelY();
        }

        if (lWidth <= 0 || lHeight <= 0)
            return;

        long curTime = System.currentTimeMillis();
        if (lastTimeMs < 0) {
            lastTimeMs = curTime;
            return;
        }

        long elapsedMs  = curTime - lastTimeMs;
        lastTimeMs      = curTime;

        int c           = 600;
        lVx             += ((elapsedMs * lAx) / c) * pixelsPerMeter;
        lVy             += ((elapsedMs * lAy) / c) * pixelsPerMeter;
        lBallX          += ((lVx * elapsedMs) / c) * pixelsPerMeter;
        lBallY          += ((lVy * elapsedMs) / c) * pixelsPerMeter;

        // ball enters hole
        if ((lBallY > 1580) && (lBallY < 1620) && (lBallX > 530) && (lBallX < 570)){
            lBallX  = ball.setBallPixelX(550);
            lBallY  = ball.setBallPixelY(200);
            lVx     = ball.setVelocityX(0);
            lVy     = ball.setVelocityY(0);
            updateScore();
        }

        // check upper wall hits
        if((lBallX > 0) && (lBallX < 710)) { // in wall x limits
            if ((lBallY > 650) && (lBallY < 700)) // bottom to wall
                reactToHit(700);
             else if ((lBallY > 600) && (lBallY < 650)) // top to wall
                reactToHit(600);
        }
        // check bottom wall hits
        if((lBallX > 450) && (lBallX < 1110)) { // in wall x limits
            if ((lBallY > 1250) && (lBallY < 1300)) // bottom to wall
                reactToHit(1300);
             else if ((lBallY > 1200) && (lBallY < 1250)) // top to wall
                reactToHit(1200);
        }

        // ball at left or upper screen limits
        boolean outsideTop = lBallY - ballRadius < 0;
        boolean outsideBottom = lBallY + ballRadius > lHeight;
        if(outsideTop || lBallX - ballRadius < 0){
            if(outsideTop)
                reactToHit(ballRadius);
            else {
                lBallX = ballRadius;
                lVx = ball.setBouncedX(true);
            }
        // ball at right or bottom screen limits
        } else if(outsideBottom || lBallX + ballRadius > lWidth){
            if(outsideBottom)
                reactToHit(lHeight - ballRadius);
            else {
                lBallX = lWidth - ballRadius;
                lVx = ball.setBouncedX(true);
            }
        }

        if (ball.isBouncedY() && Math.abs(lVy) < STOP_BOUNCING_VELOCITY) {
            lVy = 0;
            ball.setBouncedY(false);
        } else if(ball.isBouncedX() && Math.abs(lVx) < STOP_BOUNCING_VELOCITY){
            lVx = 0;
            ball.setBouncedX(false);
        }

        synchronized (ball.getLOCK()) {
            ball    .setBallPixelX(lBallX);
            ball    .setBallPixelY(lBallY);
            ball    .setVelocityX(lVx);
            ball    .setVelocityY(lVy);
        }
    }

}
