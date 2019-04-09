package com.example.shreya.makememories.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MemoryEntity::class), version = 3)
abstract class MemoryDatabase: RoomDatabase() {

    abstract fun memoryDao(): MemoryDao
    companion object {
        private var INSTANCE: MemoryDatabase? = null
        fun getInstance(context: Context): MemoryDatabase? {
            if (INSTANCE == null) {
                synchronized(MemoryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),MemoryDatabase::class.java, "memories_db")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}