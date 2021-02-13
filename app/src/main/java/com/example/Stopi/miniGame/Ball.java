package com.example.Stopi.miniGame;

import com.example.Stopi.objects.dataManage.KEYS;

public class Ball {

    private int pixelWidth, pixelHeight;
    private final int ballRadius;
    private float ballPixelX, ballPixelY;
    private float velocityX, velocityY;
    private float accelX, accelY;

    private boolean bouncedX , bouncedY;

    private final Object LOCK = new Object();

    //==================================================

    public Ball(int ballRadius) { this.ballRadius = ballRadius; bouncedX = bouncedY = false; }

    //==================================================

    public void setAccel(float ax, float ay) {
        synchronized (LOCK) {
            this.accelX = ax;
            this.accelY = ay;
        }
    }

    public void setSize(int width, int height) {
        synchronized (LOCK) {
            this.pixelWidth = width;
            this.pixelHeight = height;
        }
    }

    //==================================================

    public float setBallPixelX(float ballPixelX) { this.ballPixelX = ballPixelX; return ballPixelX; }

    public float setBallPixelY(float ballPixelY) { this.ballPixelY = ballPixelY; return ballPixelY; }

    public float setVelocityX(float velocityX) { this.velocityX = velocityX; return velocityX; }

    public float setVelocityY(float velocityY) { this.velocityY = velocityY; return velocityY; }

    public float setBouncedX(boolean bouncedX) {
        this.bouncedX = bouncedX;
        setVelocityX(-getVelocityX() * KEYS.REBOUND);
        return getVelocityX();
    }

    public float setBouncedY(boolean bouncedY) {
        this.bouncedY = bouncedY;
        setVelocityY(-getVelocityY() * KEYS.REBOUND);
        return getVelocityY();
    }

    //==================================================

    public float getBallPixelX() { return ballPixelX; }

    public float getBallPixelY() { return ballPixelY; }

    public float getVelocityX() { return velocityX; }

    public float getVelocityY() { return velocityY; }

    public boolean isBouncedX() { return bouncedX; }

    public boolean isBouncedY() { return bouncedY; }

    public float getAccelX() { return accelX; }

    public float getAccelY() { return accelY; }

    public int getPixelWidth() { return pixelWidth; }

    public int getPixelHeight() { return pixelHeight; }

    public int getBallRadius() { return ballRadius; }

    public Object getLOCK() { return LOCK; }

}
