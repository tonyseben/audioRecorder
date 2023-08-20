package com.example.audiorecorder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VRApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}