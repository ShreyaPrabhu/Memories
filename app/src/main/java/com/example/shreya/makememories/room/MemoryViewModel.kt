package com.example.shreya.makememories.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MemoryViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: MemoryRepository = MemoryRepository(application)
    private val allNotes: LiveData<List<MemoryEntity>> = repository.getAllMemos()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private var showSnackBarEventValue = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = showSnackBarEventValue

    fun doneShowingSnackBarEvent(){
        showSnackBarEventValue.value = false
    }


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

    fun getAllMemos(): LiveData<List<MemoryEntity>> {
        return allNotes
    }

    fun getSize(): Long {
        return repository.getSize()
    }

    fun getMemoryById(key: Long): MemoryEntity {
        return repository.getMemById(key)
    }

    fun deleteMemoryById(key: Long) {
        repository.deleteMemById(key)
        showSnackBarEventValue.value = true
    }

    // Cancel all coroutines
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}