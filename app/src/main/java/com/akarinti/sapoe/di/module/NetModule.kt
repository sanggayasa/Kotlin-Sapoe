package com.akarinti.sapoe.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetModule{


    @Provides
    @Singleton
    fun provideGson():Gson = Gson()
}