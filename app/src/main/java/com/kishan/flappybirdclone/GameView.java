// GameView
package com.kishan.flappybirdclone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;


public class GameView extends SurfaceView implements Runnable {

    private  Thread gameThread;
    private volatile boolean isPlaying = true;

    private SurfaceHolder holder;

    private Paint paint;

    private Bitmap background,bird,retryButton;

    private float birdX, birdY, birdVelocity;
    private final float gravity = 2.0f; //gravity for bird

    private final int screenWidth;
    private final int screenHeight;

    private ArrayList<Pipe> pipes = new ArrayList<>();
    private long lastPipeTime = 0;
    private int pipeInterval = 2000; // Milliseconds

    private Vibrator vibrator;

    private int score = 0; // Track the current  score
    private Paint scorePaint; // Paint object for rendering the score
    private Paint highScorePaint;
    private int highScore = 0; // High score

    // playing audio
    private SoundManager soundManager;
    private SharedPreferencesManager sharedPreferencesManager;
    private Rect retryButtonRect;

    public GameView(Context context) {
        super(context);
        //Initialize sharedPreferenceManager object
        sharedPreferencesManager = new SharedPreferencesManager(context);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                resume();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        paint = new Paint();

        // Load Assets
        background = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        bird = BitmapFactory.decodeResource(getResources(),(R.drawable.bird));
        retryButton = BitmapFactory.decodeResource(getResources(), R.drawable.retry);

        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, false);
        retryButton = Bitmap.createScaledBitmap(retryButton, 100,100, false);

        retryButtonRect = new Rect(screenWidth / 2 - retryButton.getWidth() / 2, screenHeight / 2 + 100 - retryButton.getHeight() / 2,
                screenWidth / 2 + retryButton.getWidth() / 2, screenHeight / 2 + 100 + retryButton.getHeight() / 2);

        //Initialize bird position
        birdX = screenWidth/4.0f; //Slight offset from center-left
        birdY = screenHeight/2.0f;
        birdVelocity = 0;

        // Initialize Vibrator
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize score paint
        scorePaint = new Paint();
        scorePaint.setColor(getResources().getColor(R.color.score)); // Set the score color
        scorePaint.setTextSize(80); // Set the text size
        scorePaint.setAntiAlias(true); // Smooth edges
        scorePaint.setTextAlign(Paint.Align.CENTER); // Center-align the text

        highScorePaint = new Paint();
        highScorePaint.setColor(getResources().getColor(R.color.score)); // Set the score color
        highScorePaint.setTextSize(30); // Set the text size
        highScorePaint.setAntiAlias(true); // Smooth edges
        highScorePaint.setTextAlign(Paint.Align.CENTER); // Center-align the text

        // Initialize SharedPreferences
        highScore = sharedPreferencesManager.getHighScorePreference();
        soundManager = new SoundManager(context);



    }

    @Override
    public void run() {
        //Log.d("GameView", "run: gameloop started");
        while (isPlaying){
            if (!holder.getSurface().isValid()) {
                    continue;
            }
          //  Log.d("GameView", "run: gameloop running");
            update();
            draw();
            control();

        }
    }

    public void update(){
        //
        birdVelocity += gravity;
        birdY += birdVelocity;

        //Prevent the bird form going off screen
        if (birdY < 0) birdY = 0;
        if (birdY + bird.getHeight() > getHeight()) {
            birdY = getHeight() - bird.getHeight();
            isPlaying = false; // Game over if bird touches the bottom
            soundManager.playDieSound(); //play die sound
        }
        if (checkCollision()){
            isPlaying=false;
            soundManager.playHitSound();
            // Trigger vibration for 500ms
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(300); // Vibrate for 500 milliseconds
            }
            // Save the high score if the current score exceeds it
            if (score > highScore) {
                highScore = score;
                sharedPreferencesManager.saveHighScorePreference(highScore);
            }
        }

    }

    private void draw(){
        //Log.d("GameView", "draw:running ");
        if (holder.getSurface().isValid()){
            Canvas canvas = holder.lockCanvas();

            Matrix matrix = new Matrix();
            float scaleX = (float) canvas.getWidth() / background.getWidth();
            float scaleY = (float) canvas.getHeight() / background.getHeight();

            // Use the larger scale factor to fill the screen completely
            float scale = Math.max(scaleX, scaleY);
            matrix.postScale(scale, scale);

            // Center the image
            float dx = (canvas.getWidth() - background.getWidth() * scale) / 2;
            float dy = (canvas.getHeight() - background.getHeight() * scale) / 2;
            matrix.postTranslate(dx, dy);

            generatePipes();
            movePipes();
            // Draw the scaled bitmap
            canvas.drawBitmap(background, matrix, null);

            // Draw pipes
            for (Pipe pipe : pipes) {
                pipe.draw(canvas, paint);
            }

            //Draw bird
            canvas.drawBitmap(bird,birdX,birdY,paint);

            // Draw score
            canvas.drawText("High Score: " + String.valueOf(highScore), screenWidth / 2.0f, 50, highScorePaint);
            canvas.drawText(String.valueOf(score), screenWidth / 2.0f, 120, scorePaint);

            // Draw resume and retry buttons if paused
            if (!isPlaying) {
                canvas.drawBitmap(retryButton, retryButtonRect.left, retryButtonRect.top, paint);
            }
            holder.unlockCanvasAndPost(canvas);
            //Log.d("GameView", "draw: if running");
        }

    }
    private void control() {
        long startTime = System.nanoTime();
        long sleepTime = (1000000000 / 60) - (System.nanoTime() - startTime);
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime / 1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        isPlaying = false;
        if (gameThread != null) {
            try {
                gameThread.join(1000); // Timeout to prevent infinite waiting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume(){
        isPlaying = true;
        if(gameThread==null || !gameThread.isAlive()){
            gameThread = new Thread(this);
            gameThread.start();
            Log.d("GameView", "resume: GameThread Started");
        }
    }
    private void restartGame() {
        pipes.clear();
        birdY = screenHeight / 2.0f; // Reset bird position
        birdVelocity = 0; // Reset velocity
        score = 0; // Reset score
        isPlaying = true; // Reset game over state
        resume();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Check if retry button is clicked
            if (!isPlaying && retryButtonRect.contains((int) event.getX(), (int) event.getY())) {
                Log.d("GameView", "onTouchEvent: retry");
                restartGame();
                return true;
            }
            // Other touch events
            if (isPlaying) {
                birdVelocity = -25; // Apply an upward force
                soundManager.playFlapSound();
            }
        }
        return true;
    }
    private void generatePipes() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPipeTime > pipeInterval) {
            pipes.add(new Pipe(getResources(), screenWidth, screenHeight));
            lastPipeTime = currentTime;
        }
    }

    private void movePipes() {
        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.move(10); // Adjust speed

            // Check if the bird passes the pipe
            if (!pipe.isPassed() && birdX > pipe.getX() + pipe.getPipeWidth()/2.0) {
                score++; // Increment score
                pipe.setPassed(true); // Mark the pipe as passed
            }

            if (pipe.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    private boolean checkCollision() {
        // Bird's center and radius
        float birdCenterX = birdX + bird.getWidth() / 2.0f;
        float birdCenterY = birdY + bird.getHeight() / 2.0f;
        float birdRadius = Math.min(bird.getWidth(), bird.getHeight()) / 2.0f;

        for (Pipe pipe : pipes) {
            // Top pipe rectangle
            Rect topRect = pipe.getTopRect();
            if (circleIntersectsRect(birdCenterX, birdCenterY, birdRadius, topRect)) {
                return true;
            }

            // Bottom pipe rectangle
            Rect bottomRect = pipe.getBottomRect();
            if (circleIntersectsRect(birdCenterX, birdCenterY, birdRadius, bottomRect)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a circle intersects a rectangle.
     *
     * @param cx Circle's center X
     * @param cy Circle's center Y
     * @param radius Circle's radius
     * @param rect The rectangle to check
     * @return True if the circle intersects the rectangle, false otherwise
     */
    private boolean circleIntersectsRect(float cx, float cy, float radius, Rect rect) {
        // Find the closest point on the rectangle to the circle's center
        float closestX = Math.max(rect.left, Math.min(cx, rect.right));
        float closestY = Math.max(rect.top, Math.min(cy, rect.bottom));

        // Calculate the distance between the circle's center and this closest point
        float distanceX = cx - closestX;
        float distanceY = cy - closestY;

        // If the distance is less than the radius, there's an intersection
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }


}
