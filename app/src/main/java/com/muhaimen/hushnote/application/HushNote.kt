package com.muhaimen.hushnote.application

import android.app.Application
import com.google.firebase.FirebaseApp

class HushNote:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}