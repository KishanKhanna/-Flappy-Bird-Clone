// SoundManger
package com.kishan.flappybirdclone;

import android.content.Context;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {
    private final MediaPlayer backgroundMusic;
    private final SoundPool soundPool;
    private final int soundFlap;
    private final int soundHit;
    private final int soundDie;
    private boolean isSoundEnabled;
    private boolean isMusicEnabled;
    SharedPreferencesManager sharedPreferencesManager;


    public SoundManager(Context context) {
        // Initialize sound pool
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build())
                .build();
        //inititalising sharedPreferencesManager object
        sharedPreferencesManager = new SharedPreferencesManager(context);

        // Load sound effects
        soundFlap = soundPool.load(context, R.raw.flap, 1);
        soundHit = soundPool.load(context, R.raw.hit, 1);
        soundDie = soundPool.load(context, R.raw.die, 1);

        // Initialize background music
        backgroundMusic = MediaPlayer.create(context, R.raw.main);
        backgroundMusic.setLooping(true);
        isMusicEnabled = sharedPreferencesManager.getAudioPreference("MusicEnabled", true);
        isSoundEnabled = sharedPreferencesManager.getAudioPreference("SoundEnabled", true);
    }

    public void playSound(int sound) {
        if (isSoundEnabled) {
            soundPool.play(sound, 1, 1, 0, 0, 1);
        }
    }

    public void playFlapSound() {
        if (isSoundEnabled)playSound(soundFlap);
    }

    public void playHitSound() {
        if (isSoundEnabled)playSound(soundHit);
    }

    public void playDieSound() {
        if (isSoundEnabled)playSound(soundDie);
    }

    public void playMusic() {
        if (isMusicEnabled && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    public void pauseMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void stopMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    public void setMusicEnabled(boolean enabled) {
        isMusicEnabled = enabled;
        if (enabled) {
            playMusic();
        } else {
            pauseMusic();
        }
    }


    public void release() {
        soundPool.release();
        if (backgroundMusic != null) {
            backgroundMusic.release();
        }
    }
}

