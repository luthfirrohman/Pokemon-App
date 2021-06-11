package com.luthfirrohman.movieapps

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.luthfirrohman.movieapps.core.di.databaseModule
import com.luthfirrohman.movieapps.core.di.networkModule
import com.luthfirrohman.movieapps.core.di.repositoryModule
import com.luthfirrohman.movieapps.di.useCaseModule
import com.luthfirrohman.movieapps.di.viewModelModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}