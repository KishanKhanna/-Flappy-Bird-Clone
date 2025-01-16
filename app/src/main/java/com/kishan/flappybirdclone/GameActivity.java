package com.kishan.flappybirdclone;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gameView= new GameView(this);
        setContentView(gameView);

    }

    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause(); // onPause the game when the activity is not active
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume(); // onPause the game when the activity is not active
    }
}