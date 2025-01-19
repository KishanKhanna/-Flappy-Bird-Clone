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
    private  int pipeWidth; // Adjusted to match the new pipe width
    private int gap;

    private final int screenHeight;
    private boolean passed = false;


    // Minimum height for the visible portion of each pipe
    private static int MIN_PIPE_HEIGHT;

    public Pipe(Resources res, int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;

        // Load pipe bitmaps
        topPipe = BitmapFactory.decodeResource(res, R.drawable.pipe_up);
        bottomPipe = BitmapFactory.decodeResource(res, R.drawable.pipe_down);

        pipeWidth= (int) (screenWidth * (0.225f));
        MIN_PIPE_HEIGHT= screenHeight/7;
        // Scale bitmaps to match the new pipe dimensions
        topPipe = Bitmap.createScaledBitmap(topPipe, pipeWidth, screenHeight-gap-MIN_PIPE_HEIGHT*2, false);
        bottomPipe = Bitmap.createScaledBitmap(bottomPipe, pipeWidth, screenHeight-gap-MIN_PIPE_HEIGHT*2, false);

        // Ensure there's always a visible portion of each pipe by constraining pipeStartY
        gap = (int) (screenHeight * 0.2f); // Adjust gap size
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
        return pipeWidth;
    }

    public void move(int speed) {
        x -= speed; // Move left
    }

    public boolean isOffScreen() {
        return x + pipeWidth < 0;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(topPipe, x, topY, paint);
        canvas.drawBitmap(bottomPipe, x, bottomY, paint);
    }

    public Rect getTopRect() {
        return new Rect(x, topY, x + pipeWidth, topY + topPipe.getHeight());
    }

    public Rect getBottomRect() {
        return new Rect(x, bottomY, x + pipeWidth, bottomY + bottomPipe.getHeight());
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

}