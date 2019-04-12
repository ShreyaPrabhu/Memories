package com.example.shreya.makememories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shreya.makememories.room.MemoryEntity
import com.example.shreya.makememories.room.MemoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MemoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemoryRepository = MemoryRepository(application)

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    fun insert(memoryEntity: MemoryEntity) {
        uiScope.launch {
            repository.insert(memoryEntity)
        }
    }

    fun deleteAllMemos() {
        uiScope.launch {
            repository.deleteAllMemos()
        }
    }

    fun getMemoryById(key: Long): MemoryEntity {
        return repository.getMemById(key)
    }

    fun deleteMemoryById(key: Long) {
        MainViewModel.memoryEntity = repository.getMemById(key)
        repository.deleteMemById(key)
        MainViewModel.showSnackBarEventValue.value = true
    }

    // Cancel all coroutines
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}