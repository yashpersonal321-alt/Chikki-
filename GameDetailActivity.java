package com.example.gamehub;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameDetailActivity extends AppCompatActivity {
    
    private TextView tvGameName, tvPlayTime;
    private Button btnPlay, btnSetCategory;
    private DatabaseHelper dbHelper;
    private String packageName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        
        dbHelper = new DatabaseHelper(this);
        
        tvGameName = findViewById(R.id.tvGameName);
        tvPlayTime = findViewById(R.id.tvPlayTime);
        btnPlay = findViewById(R.id.btnPlay);
        btnSetCategory = findViewById(R.id.btnSetCategory);
        
        // Intent से डाटा लें
        packageName = getIntent().getStringExtra("packageName");
        String gameName = getIntent().getStringExtra("gameName");
        
        tvGameName.setText(gameName);
        
        long playTime = dbHelper.getPlayTime(packageName);
        long minutes = playTime / 60000;
        tvPlayTime.setText("कुल खेला: " + minutes + " मिनट");
        
        btnPlay.setOnClickListener(v -> {
            PackageManager pm = getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        });
        
        btnSetCategory.setOnClickListener(v -> {
            // यहां कैटेगरी सेट करने का डायलॉग दिखाएं
            // अभी सिर्फ टोस्ट दिखा रहे हैं
            android.widget.Toast.makeText(this, "कैटेगरी सेट करें", android.widget.Toast.LENGTH_SHORT).show();
        });
    }
}
