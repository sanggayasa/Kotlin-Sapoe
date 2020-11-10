package com.akarinti.sapoe.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule{

    @Provides
    @Singleton
    fun applicationContext(application:Application):Context = application
}