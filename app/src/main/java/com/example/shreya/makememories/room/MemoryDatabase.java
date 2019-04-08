package com.example.shreya.makememories.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MemoryEntity.class}, version = 2)
public abstract class MemoryDatabase extends RoomDatabase {
    private static MemoryDatabase memoryDatabase;
    public abstract MemoryDao memoryDao();
    private Context context;
    public static MemoryDatabase getInstance(Context context){
        if(memoryDatabase== null){
            memoryDatabase = Room.databaseBuilder(context.getApplicationContext(), MemoryDatabase.class, "Memory-database")
                    .allowMainThreadQueries()
                    .build();
        }
        return memoryDatabase;
    }

    public static void destroyInstance() {
        memoryDatabase = null;
    }
}
