package com.akarinti.sapoe.view.main.unscheduled.answer_parent

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.response.QuestionarieResponseAcOffline
import com.akarinti.sapoe.data.response.QuestionarieResponseCleanOffline
import com.akarinti.sapoe.data.response.QuestionarieResponseNbOffline
import com.akarinti.sapoe.data.response.QuestionarieResponseOtherOffline
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class UnscheduleAnswerParentPresenter @Inject constructor(
    private val scheduleEntity: ScheduleEntity
) : BasePresenter<UnscheduleAnswerParentContract.View>(), UnscheduleAnswerParentContract.Presenter {

    override fun getQuestionaireParentAC(orderID: String) {
        addSubscription(scheduleEntity.getAnswer(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseAcOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onQuestionaireParentAc(wrapper.response.data)
                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message?:""), it.code())
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

    override fun getQuestionaireParentNB(orderID: String) {
        addSubscription(scheduleEntity.getAnswer(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseNbOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onQuestionaireParentNb(wrapper.response.data)
                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message?:""), it.code())
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

    override fun getQuestionaireParentClean(orderID: String) {
        addSubscription(scheduleEntity.getAnswer(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseCleanOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onQuestionaireParentClean(wrapper.response.data)
                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message?:""), it.code())
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

    override fun getQuestionaireParentMcds(orderID: String) {
        addSubscription(scheduleEntity.getAnswer(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseOtherOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onQuestionaireParentMcds(wrapper.response.data)
                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message?:""), it.code())
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

    override fun getQuestionaireParentQc(orderID: String) {
        addSubscription(scheduleEntity.getAnswer(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseOtherOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onQuestionaireParentQc(wrapper.response.data)
                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message?:""), it.code())
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