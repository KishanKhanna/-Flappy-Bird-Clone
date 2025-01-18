//Main Activity
package com.kishan.flappybirdclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageButton play;
    ImageButton iconMusic;
    ImageButton iconSound;
    private SoundManager soundManager;
    boolean isMusicEnabled;
    boolean isSoundEnabled;
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "MainActivity :onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        soundManager = new SoundManager(this);

        iconMusic = findViewById(R.id.icon_music);
        iconSound = findViewById(R.id.icon_sound);
        //Initialize sharedPreferenceManager object
        sharedPreferencesManager = new SharedPreferencesManager(this);

        isMusicEnabled = sharedPreferencesManager.getAudioPreference("MusicEnabled", true);
        isSoundEnabled = sharedPreferencesManager.getAudioPreference("SoundEnabled", true);

        updateToggleStates();
        // Toggle music
        iconMusic.setOnClickListener(v -> {
            sharedPreferencesManager.saveAudioPreference("MusicEnabled", !sharedPreferencesManager.getAudioPreference("MusicEnabled", true));
            updateToggleStates();
        });

        // Toggle sound
        iconSound.setOnClickListener(v -> {
            sharedPreferencesManager.saveAudioPreference("SoundEnabled", !sharedPreferencesManager.getAudioPreference("SoundEnabled", true));
            updateToggleStates();
        });
        play= findViewById(R.id.imageButton);
        play.setOnClickListener(v -> startActivity(new Intent(this, GameActivity.class)));


        // update highscore
        loadHighScore();
    }

    @Override
    protected void onPause(){
        Log.d("TAG", "MainActivity:onPause");
        super.onPause();
        soundManager.pauseMusic();
    }
    @Override
    protected void onResume() {
        Log.d("TAG", "MainActivity :onResume: ");
        super.onResume();
        loadHighScore();
        soundManager.playMusic();
        updateToggleStates();

   }
    private void loadHighScore() {
        int highScore = sharedPreferencesManager.getHighScorePreference(); // Default to 0 if not set

        // Display the high score (optional)
        TextView highScoreTextView = findViewById(R.id.high_score);
        highScoreTextView.setText("High Score: " + highScore);
    }
    @Override
    protected void onDestroy() {
        Log.d("TAG", "MainActivity :onDestroy: ");
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release();
            finishAffinity();
        }
    }

    private void updateToggleStates() {
        Log.d("TAG", "updateToggleStates: run");

        isMusicEnabled = sharedPreferencesManager.getAudioPreference("MusicEnabled", true);
        isSoundEnabled = sharedPreferencesManager.getAudioPreference("SoundEnabled", true);

        // Update SoundManager

        Log.d("TAG", "updateToggleStates:setMusicEnabled"+ (isMusicEnabled));
        soundManager.setMusicEnabled(isMusicEnabled);

        // Update icons
        iconMusic.setImageResource(isMusicEnabled ? R.drawable.music_enabled : R.drawable.music_disabled);
        iconSound.setImageResource(isSoundEnabled ? R.drawable.sound_enabled : R.drawable.sound_disabled);
    }


}