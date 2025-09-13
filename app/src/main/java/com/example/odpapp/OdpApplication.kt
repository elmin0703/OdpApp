package com.example.odpapp

import android.app.Application
import com.example.odpapp.data.AppContainer
import com.example.odpapp.data.AppDatabase
import com.example.odpapp.data.DefaultAppContainer

class OdpApplication : Application()  {
    lateinit var container : AppContainer
    //val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}