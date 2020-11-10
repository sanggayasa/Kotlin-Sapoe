package com.akarinti.sapoe.view.main.home

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.TaskEntity
import com.akarinti.sapoe.data.response.NewsListResponse
import com.akarinti.sapoe.data.response.OrderTicketCountResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val taskEntity: TaskEntity
) : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun getMenuCount() {
        addSubscription(taskEntity.getOrderTicketCount().uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(OrderTicketCountResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onMenuCount(wrapper.response.data.detail)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {})
        )
    }

    override fun getNews(page: Int, limit: Int) {
        addSubscription(taskEntity.getNewsList(page, limit).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(NewsListResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onNews(wrapper.response)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {})
        )
    }
}