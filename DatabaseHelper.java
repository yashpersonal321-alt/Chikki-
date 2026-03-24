package com.example.gamehub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "GameHub.db";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TABLE_PLAYTIME = "playtime";
    private static final String COLUMN_PACKAGE = "package_name";
    private static final String COLUMN_TIME = "play_time";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PLAYTIME + " (" +
                COLUMN_PACKAGE + " TEXT PRIMARY KEY, " +
                COLUMN_TIME + " LONG)";
        db.execSQL(createTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYTIME);
        onCreate(db);
    }
    
    public void updatePlayTime(String packageName, long time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PACKAGE, packageName);
        values.put(COLUMN_TIME, time);
        
        // अगर पहले से है तो अपडेट करें
        if (getPlayTime(packageName) == 0) {
            db.insert(TABLE_PLAYTIME, null, values);
        } else {
            db.update(TABLE_PLAYTIME, values, COLUMN_PACKAGE + "=?", new String[]{packageName});
        }
        db.close();
    }
    
    public long getPlayTime(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAYTIME, new String[]{COLUMN_TIME},
                COLUMN_PACKAGE + "=?", new String[]{packageName},
                null, null, null);
        
        long time = 0;
        if (cursor != null && cursor.moveToFirst()) {
            time = cursor.getLong(0);
            cursor.close();
        }
        return time;
    }
}
