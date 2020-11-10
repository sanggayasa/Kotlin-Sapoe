package com.akarinti.sapoe.view.splash

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.TokenEntity
import com.akarinti.sapoe.data.response.TokenResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SplashPresenter @Inject constructor(
    private val tokenEntity: TokenEntity
) : BasePresenter<SplashContract.View>(), SplashContract.Presenter {
    override fun getToken() {
        if (headerManager.hasToken()) {
            getWhitelistApp()
        } else {
            addSubscription(tokenEntity.getOauthToken().uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(TokenResponse::class.java))
                if (null != wrapper.response?.data) {
                    headerManager.authToken = wrapper.response.data
                    getWhitelistApp()
                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
                }
            }, { view?.errorScreen(it.message) }, {}))
        }
    }

    private fun getWhitelistApp() {
        addSubscription(tokenEntity.getWhitelist().uiSubscribe({
            if (it.code() == 200) {
                it.body()?.data?.list?.let { list ->
                    headerManager.appList.clear()
                    for (i in list) {
                        headerManager.appList.add(i.appName?:"-")
                    }
                }
                view?.onNextScreen(headerManager.isLoggedIn())
            } else {
                val err = it.errorBody()?.string()
                view?.errorScreen(err?.getErrorMessage(it.message()))
            }
        }, { view?.errorScreen(it.message) }, {}))
    }
}