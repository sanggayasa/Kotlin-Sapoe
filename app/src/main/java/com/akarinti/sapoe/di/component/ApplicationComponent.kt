package com.akarinti.sapoe.di.component

import android.app.Application
import com.akarinti.sapoe.SapoeApp
import com.akarinti.sapoe.di.builder.ActivityBuilder
import com.akarinti.sapoe.di.module.ApplicationModule
import com.akarinti.sapoe.di.module.NetModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class,
    NetModule::class, ActivityBuilder::class,
    AndroidInjectionModule::class))
interface ApplicationComponent{

    fun Inject(scutoApp: SapoeApp)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application:Application):Builder

        fun build():ApplicationComponent
    }


}