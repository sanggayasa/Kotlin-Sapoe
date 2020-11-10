package com.akarinti.sapoe.view.login

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.AuthEntity
import com.akarinti.sapoe.data.response.LoginResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val authEntity: AuthEntity
) : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    override fun loginUser(username: String, password: String, deviceImei:String) {
        addSubscription(authEntity.loginUser(username, password, deviceImei).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(LoginResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                headerManager.accessToken = wrapper.response.data.detail?.toAccessToken()
                headerManager.profileRepository.profile = wrapper.response.data.detail
                view?.gotoNextPage()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {view?.errorScreen(it.message)}, {}))
    }
}