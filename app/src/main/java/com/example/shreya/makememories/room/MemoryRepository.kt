package com.example.shreya.makememories.room

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

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

    fun insert(memoryEntity: MemoryEntity) {
        val insertMemoAsyncTask = InsertMemoAsyncTask(memoryDao).execute(memoryEntity)
    }

    fun deleteAllMemos() {
        val deleteAllMemosAsyncTask = DeleteAllMemosAsyncTask(
                memoryDao
        ).execute()
    }

    fun getAllMemos(): LiveData<List<MemoryEntity>> {
        return allMemories
    }

    fun getSize() : Long {
        return memoryDao.getSize()
    }

    fun getMemById(key: Long) : MemoryEntity? {
        return memoryDao.get(key)
    }

    fun deleteMemById(key: Long){
        return memoryDao.deleteById(key)
    }

    private class InsertMemoAsyncTask(memoryDao: MemoryDao) : AsyncTask<MemoryEntity, Unit, Unit>() {
        val memoDao = memoryDao

        override fun doInBackground(vararg p0: MemoryEntity?) {
            memoDao.insert(p0[0]!!)
        }
    }

    private class DeleteAllMemosAsyncTask(val memoDao: MemoryDao) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg p0: Unit?) {
            memoDao.clear()
        }
    }

}