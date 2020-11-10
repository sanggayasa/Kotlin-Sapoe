package com.akarinti.sapoe.base

import com.akarinti.sapoe.BuildConfig
import com.akarinti.sapoe.data.entity.VersionEntity
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.response.VersionResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.HttpURLConnection
import javax.inject.Inject

open class BasePresenter<T: ErrorView>{

    @Inject
    lateinit var headerManager: HeaderManager

    @Inject
    lateinit var versionEntity: VersionEntity

    var compose: CompositeDisposable = CompositeDisposable()

    var view:T ?= null

    open fun detachView(){
        this.view = null
        compose.clear()
    }

    fun addSubscription(disposable: Disposable) = compose.add(disposable)

    fun clearAllSubscription() = compose.clear()

    fun forceLogout() {
        headerManager.logout()
    }

    fun checkVersion() {
        addSubscription(versionEntity.checkVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
            .uiSubscribe({ response ->
                val wrapper = response.convertResponse(TypeToken.get(VersionResponse::class.java))
                if (null != wrapper.response?.data && response.code() == HttpURLConnection.HTTP_OK) {
                    view?.onVersionCheck(wrapper.response.data)
                }}, {}, {}))
    }

}