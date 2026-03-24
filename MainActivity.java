package com.example.gamehub;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private List<Game> allGames;
    private TabLayout tabLayout;
    private DatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbHelper = new DatabaseHelper(this);
        
        recyclerView = findViewById(R.id.recyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        
        // ग्रिड लेआउट (2 कॉलम)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        // सभी गेम्स लोड करें
        loadGames();
        
        // टैब लेआउट सेटअप
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterGamesByCategory(tab.getPosition());
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void loadGames() {
        allGames = new ArrayList<>();
        PackageManager pm = getPackageManager();
        
        // सभी इंस्टॉल्ड ऐप्स की लिस्ट
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        
        for (ApplicationInfo appInfo : packages) {
            // सिर्फ गेम्स फ़िल्टर करें
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // सिस्टम ऐप नहीं
                try {
                    // चेक करें कि यह गेम है या नहीं
                    if (isGameApp(appInfo.packageName)) {
                        String appName = pm.getApplicationLabel(appInfo).toString();
                        Drawable appIcon = pm.getApplicationIcon(appInfo);
                        
                        Game game = new Game(appName, appInfo.packageName, appIcon);
                        
                        // डेटाबेस से प्ले टाइम लोड करें
                        long playTime = dbHelper.getPlayTime(appInfo.packageName);
                        game.setTotalPlayTime(playTime);
                        
                        allGames.add(game);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        gameAdapter = new GameAdapter(allGames, this);
        recyclerView.setAdapter(gameAdapter);
        
        Toast.makeText(this, "मिले " + allGames.size() + " गेम्स", Toast.LENGTH_SHORT).show();
    }
    
    private boolean isGameApp(String packageName) {
        // गेम्स की पहचान करने का तरीका
        // 1. पैकेज नाम से (जैसे com.xyz.game)
        // 2. या CATEGORY_GAME से
        
        if (packageName.contains("game") || 
            packageName.contains("Game") ||
            packageName.contains("gameloft") ||
            packageName.contains("miniclip")) {
            return true;
        }
        
        // आप चाहें तो यहां और भी कंडीशन लगा सकते हैं
        return false;
    }
    
    private void filterGamesByCategory(int category) {
        // यहां आप कैटेगरी के हिसाब से फ़िल्टर कर सकते हैं
        // अभी सभी गेम्स दिखा रहे हैं
        gameAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // जब वापस आएं तो प्ले टाइम अपडेट करें
        if (gameAdapter != null) {
            for (Game game : allGames) {
                long updatedTime = dbHelper.getPlayTime(game.getPackageName());
                game.setTotalPlayTime(updatedTime);
            }
            gameAdapter.notifyDataSetChanged();
        }
    }
}
