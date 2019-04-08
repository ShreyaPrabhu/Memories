package com.example.shreya.makememories.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class MemoryViewModel(application: Application) : AndroidViewModel(application) {


    private var repository: MemoryRepository = MemoryRepository( application)
    private var allNotes: LiveData<List<MemoryEntity>>  = repository.getAllMemos()


    fun insert(memoryEntity: MemoryEntity) {
        repository.insert(memoryEntity)
    }

    fun deleteAllMemos() {
        repository.deleteAllMemos()
    }

    fun getAllMemos(): LiveData<List<MemoryEntity>> {
        return allNotes
    }

    fun getSize():Long {
        return repository.getSize()
    }

    fun getMemoryById(key: Long): MemoryEntity? {
        return repository.getMemById(key)
    }

    fun deleteMemoryById(key: Long) {
        repository.deleteMemById(key)
    }
}