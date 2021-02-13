package com.example.Stopi.miniGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.Stopi.objects.dataManage.KEYS;

public class Maze {

    private Context context;

    private Paint backgroundPaint;
    private Paint ballPaint;
    private Paint linePaint;
    private Paint holePaint;

    public Maze(Context context) { this.context = context; initColors(); }

    //=========================================

    private void initColors(){
        backgroundPaint = createPaint(Color.WHITE);
        ballPaint = createPaint(Color.RED);
        holePaint = createPaint(Color.GRAY);

        linePaint = createPaint(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(90/context.getResources().getDisplayMetrics().density);
    }

    private Paint createPaint(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    public void createMaze(Canvas c, float ballX, float ballY) {
        c.drawCircle(ballX, ballY, KEYS.BALL_RADIUS, ballPaint);
        c.drawLine(0, 650, 700, 650, linePaint);
        c.drawLine(450, 1250, 1200, 1250, linePaint);
        c.drawCircle(550,1600,50,holePaint);
    }

    //=========================================

    public Paint getBackgroundPaint() { return backgroundPaint; }

    public Paint getBallPaint() { return ballPaint; }

    public Paint getLinePaint() { return linePaint; }

    public Paint getHolePaint() { return holePaint; }

}
