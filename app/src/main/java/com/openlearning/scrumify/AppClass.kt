package com.openlearning.scrumify

import android.app.Application
import android.widget.EditText

class AppClass : Application() {

    companion object {
        lateinit var instance: AppClass
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}