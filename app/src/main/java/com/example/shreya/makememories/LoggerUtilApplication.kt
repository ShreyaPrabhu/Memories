package com.example.shreya.makememories

import android.app.Application
import timber.log.Timber

class LoggerUtilApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}