package com.example.shreya.makememories.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memory: MemoryEntity)

    @Update
    fun update(memory: MemoryEntity)

    @Query("SELECT * from memories_table WHERE memory_id = :key")
    fun get(key: Long): MemoryEntity

    @Query("DELETE FROM memories_table")
    fun clear()

    @Query("DELETE FROM memories_table where memory_id = :key")
    fun deleteById(key: Long)

    @Query("SELECT * FROM memories_table ORDER BY memory_id DESC")
    fun getAllMemories(): LiveData<List<MemoryEntity>>

    @Query("SELECT count(*) from memories_table")
    fun getSize(): Long

}