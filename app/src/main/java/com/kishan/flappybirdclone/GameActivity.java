//Game Activity
package com.kishan.flappybirdclone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private SoundManager soundManager;
    private SharedPreferencesManager sharedPreferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "GameActivity :onCreate: ");
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Initialize sharedPreferenceManager object
        sharedPreferencesManager = new SharedPreferencesManager(this);

        gameView= new GameView(this);
        setContentView(gameView);

        // Play background music
        soundManager = new SoundManager(this);
        soundManager.playMusic();

    }

    @Override
    protected void onPause(){
        Log.d("TAG", "GameActivity :onPause: ");
        super.onPause();
        gameView.pause(); // onPause the game when the activity is not active
        soundManager.stopMusic();

    }

    @Override
    protected void onResume(){
        Log.d("TAG", "GameActivity :onResume: ");
        super.onResume();
        gameView.resume(); // onPause the game when the activity is not active
    }
    @Override
    protected void onDestroy(){
        Log.d("TAG", "GameActivity:onDestroy");
        super.onDestroy();
        soundManager.release();
    }
}