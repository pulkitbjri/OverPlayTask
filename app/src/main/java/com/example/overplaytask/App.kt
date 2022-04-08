package com.example.overplaytask

import android.annotation.SuppressLint
import android.app.Application
import com.example.overplaytask.base.di.AppComponent
import com.example.overplaytask.base.di.ComponentProvider
import com.example.overplaytask.base.di.DaggerAppComponent

class App : Application(), ComponentProvider {
    private lateinit var appComponent: AppComponent
    companion object {
        lateinit var INSTANCE: Application
    }
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        appComponent = DaggerAppComponent.factory()
            .create(this)
        appComponent.inject(this)
    }

    override fun provideAppComponent(): AppComponent {
        return appComponent
    }
}