package com.kishan.flappybirdclone;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Pipe {
    private Bitmap topPipe, bottomPipe;

    private int x, topY, bottomY;
    private static final int PIPE_WIDTH = 150; // Adjusted to match the new pipe width
    private int gap;

    private final int screenHeight;
    private boolean passed = false;


    // Minimum height for the visible portion of each pipe
    private static final int MIN_PIPE_HEIGHT = 200;

    public Pipe(Resources res, int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;

        // Load pipe bitmaps
        topPipe = BitmapFactory.decodeResource(res, R.drawable.pipe_up);
        bottomPipe = BitmapFactory.decodeResource(res, R.drawable.pipe_down);

        // Scale bitmaps to match the new pipe dimensions
        topPipe = Bitmap.createScaledBitmap(topPipe, PIPE_WIDTH, screenHeight-gap-MIN_PIPE_HEIGHT*2, false);
        bottomPipe = Bitmap.createScaledBitmap(bottomPipe, PIPE_WIDTH, screenHeight, false);

        // Ensure there's always a visible portion of each pipe by constraining pipeStartY
        gap = 300; // Adjust gap size
        int maxPipeStartY = screenHeight - gap - MIN_PIPE_HEIGHT;
        int pipeStartY = MIN_PIPE_HEIGHT + (int) (Math.random() * (maxPipeStartY - MIN_PIPE_HEIGHT));

        topY = pipeStartY - topPipe.getHeight();
        bottomY = pipeStartY + gap;

        x = screenWidth; // Start off-screen to the right
    }
    public int getX() {
        return x;
    }
    public int getPipeWidth(){
        return PIPE_WIDTH;
    }

    public void move(int speed) {
        x -= speed; // Move left
    }

    public boolean isOffScreen() {
        return x + PIPE_WIDTH < 0;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(topPipe, x, topY, paint);
        canvas.drawBitmap(bottomPipe, x, bottomY, paint);
    }

    public Rect getTopRect() {
        return new Rect(x, topY, x + PIPE_WIDTH, topY + topPipe.getHeight());
    }

    public Rect getBottomRect() {
        return new Rect(x, bottomY, x + PIPE_WIDTH, bottomY + bottomPipe.getHeight());
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}