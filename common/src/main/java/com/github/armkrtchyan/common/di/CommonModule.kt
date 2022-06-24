package com.github.armkrtchyan.common.di

import com.github.armkrtchyan.common.retrofit.RetrofitModule
import com.github.armkrtchyan.common.shared.NetworkConnection
import com.github.armkrtchyan.common.shared.PreferencesManager
import org.koin.dsl.module

val commonDataModule = module {
    single { PreferencesManager() }
    single { NetworkConnection(context = get()) }
    single { RetrofitModule(interceptors = get()).providesRetrofit() }
}