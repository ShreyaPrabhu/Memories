package com.example.shreya.makememories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shreya.makememories.room.MemoryEntity
import com.example.shreya.makememories.room.MemoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: MemoryRepository = MemoryRepository(application)
    private val allMemos: LiveData<List<MemoryEntity>> = repository.getAllMemos()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)


    companion object {
        var showSnackBarEventValue = MutableLiveData<Boolean>()

        lateinit var memoryEntity: MemoryEntity

        val showSnackBarEvent: LiveData<Boolean>
            get() = showSnackBarEventValue

        val getMemoryEntityToUndo: MemoryEntity
            get() = memoryEntity

        fun doneShowingSnackBarEvent(){
            showSnackBarEventValue.value = false
        }



    }

    fun insert(memoryEntity: MemoryEntity) {
        uiScope.launch {
            repository.insert(memoryEntity)
        }
    }


    fun getAllMemos(): LiveData<List<MemoryEntity>> {
        return allMemos
    }

    fun getSize(): Long {
        return repository.getSize()
    }

    // Cancel all coroutines
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}