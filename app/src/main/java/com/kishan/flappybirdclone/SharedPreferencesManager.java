package com.kishan.flappybirdclone;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREFS_NAME = "FlappyBird";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean getAudioPreference(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveAudioPreference(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }
    public int getHighScorePreference(){
        return sharedPreferences.getInt("HighScore",0);
    }
    public void saveHighScorePreference(int highScore) {
        editor.putInt("HighScore",highScore).apply();
    }
}
