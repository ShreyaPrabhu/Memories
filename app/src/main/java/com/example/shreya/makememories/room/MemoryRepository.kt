package com.example.shreya.makememories.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemoryRepository(application: Application) {

    private var memoryDao: MemoryDao
    private var allMemories: LiveData<List<MemoryEntity>>


    init {
        val database: MemoryDatabase = MemoryDatabase.getInstance(
                application.applicationContext
        )!!
        memoryDao = database.memoryDao()
        allMemories = memoryDao.getAllMemories()
    }

    suspend fun insert(memoryEntity: MemoryEntity) {
        return withContext(Dispatchers.IO) {
            val memoDao = memoryDao
            memoDao.insert(memoryEntity)
        }
    }

    suspend fun deleteAllMemos(){
        return withContext(Dispatchers.IO) {
            val memoDao = memoryDao
            memoDao.clear()
        }
    }

    fun getAllMemos(): LiveData<List<MemoryEntity>> {
        return allMemories
    }

    fun getSize() : Long {
        return memoryDao.getSize()
    }

    fun getMemById(key: Long) : MemoryEntity{
        return memoryDao.get(key)
    }

    fun deleteMemById(key: Long){
        return memoryDao.deleteById(key)
    }

}