package com.akarinti.sapoe

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import androidx.multidex.MultiDexApplication
import com.akarinti.sapoe.di.component.ApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

open class SapoeApp(): MultiDexApplication(), HasAndroidInjector, Application.ActivityLifecycleCallbacks {
    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var activityDispachAndroidInjection:DispatchingAndroidInjector<Activity>

    lateinit var applicationComponent: ApplicationComponent
    private var handler: Handler? = null
    private var runValidate: Runnable? = null
    private var needValidate = true

    override fun onCreate() {
        super.onCreate()
        initInjection()
        JodaTimeAndroid.init(this)
    }

    fun initInjection(){
        applicationComponent = com.akarinti.sapoe.di.component.DaggerApplicationComponent.builder().application(this).build()
        applicationComponent.Inject(this)
    }

    override fun onActivityPaused(p0: Activity?) {
        runValidate = Runnable { needValidate = true }
        handler?.postDelayed(runValidate, 1000)
    }

    override fun onActivityResumed(p0: Activity?) {
        if (!needValidate) {
            cancelValidate()
        }
    }

    override fun onActivityStarted(p0: Activity?) {}

    override fun onActivityDestroyed(p0: Activity?) {}

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {}

    override fun onActivityStopped(p0: Activity?) {}

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {}

    fun isNeedValidate(): Boolean {
        return needValidate
    }

    fun setNeedValidate(needValidate: Boolean) {
        this.needValidate = needValidate
    }

    fun cancelValidate() {
        needValidate = false
        if (runValidate != null) {
            handler?.removeCallbacks(runValidate)
            runValidate = null
        }
    }
}