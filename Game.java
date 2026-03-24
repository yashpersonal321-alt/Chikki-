package com.example.gamehub;

import android.graphics.drawable.Drawable;

public class Game {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private long totalPlayTime; // मिलीसेकंड में
    private int category; // 0=All, 1=Racing, 2=Action, 3=Puzzle etc.
    
    public Game(String appName, String packageName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.totalPlayTime = 0;
        this.category = 0;
    }
    
    // Getter और Setter
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public Drawable getAppIcon() { return appIcon; }
    public void setAppIcon(Drawable appIcon) { this.appIcon = appIcon; }
    
    public long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
    
    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }
}
