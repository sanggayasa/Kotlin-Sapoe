package com.akarinti.sapoe.view.main.other

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.AuthEntity
import com.akarinti.sapoe.data.response.LogoutResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import javax.inject.Inject

class OtherPresenter @Inject constructor(
    private val authEntity: AuthEntity
) : BasePresenter<OtherContract.View>(), OtherContract.Presenter {

    override fun logoutUser() {
        addSubscription(authEntity.logoutUser().uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(LogoutResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onLogout()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {view?.errorScreen(it.message)}, {}))
    }
}