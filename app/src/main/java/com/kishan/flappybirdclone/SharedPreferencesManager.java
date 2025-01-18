package com.kishan.flappybirdclone;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREFS_NAME = "FlappyBird";
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean getAudioPreference(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveAudioPreference(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
    public int getHighScorePreference(){
        return sharedPreferences.getInt("HighScore",0);
    }
    public void saveHighScorePreference(int highScore) {
        sharedPreferences.edit().putInt("HighScore",highScore).apply();
    }
}
