package com.github.armkrtchyan.common.base

import android.app.Application
import androidx.work.Configuration
import com.github.armkrtchyan.common.di.commonDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module

abstract class BaseApplication : Application(), Configuration.Provider {

    abstract val modules: List<Module>

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            workManagerFactory()
            modules(listOf(commonDataModule))
                .modules(modules)
        }
    }
}