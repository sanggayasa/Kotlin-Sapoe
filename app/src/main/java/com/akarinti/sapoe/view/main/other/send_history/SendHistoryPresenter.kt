package com.akarinti.sapoe.view.main.other.send_history

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.data.body.LogOrderBody
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.extension.batch
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.ParentOtherOffline
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class SendHistoryPresenter @Inject constructor(
    private var inprogressRepository: InprogressRepository,
    private val scheduleEntity: ScheduleEntity
) : BasePresenter<SendHistoryContract.View>(), SendHistoryContract.Presenter {

    var orderList: ArrayList<LogOrderBody> = arrayListOf()

    override fun sendHistory(start: Long, end: Long) {
        var answerList: ArrayList<LogOrderBody.Answer> = arrayListOf()

        inprogressRepository.scheduledCl?.filterAll(filterClean(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_SCHEDULED, temperature = i.suhu_answer, answerList = answer))
                }
            }
        }

        inprogressRepository.scheduledAc?.filterAll(filterAc(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_SCHEDULED, temperature = null, answerList = answer))
                }
            }
        }

        inprogressRepository.scheduledNb?.filterAll(filterNb(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_SCHEDULED, temperature = null, answerList = answer))
                }
            }
        }

        inprogressRepository.unscheduledCl?.filterAll(filterClean(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_UNSCHEDULED, temperature = i.suhu_answer, answerList = answer))
                }
            }
        }

        inprogressRepository.unscheduledAc?.filterAll(filterAc(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_UNSCHEDULED, temperature = null, answerList = answer))
                }
            }
        }

        inprogressRepository.unscheduledNb?.filterAll(filterNb(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_UNSCHEDULED, temperature = null, answerList = answer))
                }
            }
        }

        inprogressRepository.unscheduledMcds?.filterAll(filterOther(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_UNSCHEDULED, temperature = null, answerList = answer))
                }
            }
        }

        inprogressRepository.unscheduledQc?.filterAll(filterOther(start, end))?.let {
            for (i in it) {
                answerList.clear()
                for (child in i.itemList?: listOf()) {
                    for (form in child.formList?: listOf()) {
                        for (answer in form.questionList?: listOf()) {
                            answerList.add(LogOrderBody.Answer(child.id?:"-", answer.id?:"",
                                if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                        }
                    }
                }
                for (form in i.formList?: listOf()) {
                    for (answer in form.questionList?: listOf()) {
                        answerList.add(LogOrderBody.Answer("", answer.id?:"",
                            if ((answer.answerLocal?:"").contains("data:image/png;base64,")) "" else answer.answerLocal?:""))
                    }
                }
                answerList.asSequence().batch(200).forEach { answer ->
                    orderList.add(LogOrderBody(orderId = i.id, orderType = i.type, category = Params.CATEGORY_UNSCHEDULED, temperature = null, answerList = answer))
                }
            }
        }

        sendOrderLog()
    }

    private fun sendOrderLog() {
        if (orderList.isNotEmpty()) {
            addSubscription(scheduleEntity.submitLogHistory(orderList[0]).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
                if (it.code() == HttpURLConnection.HTTP_OK) {
                    orderList.removeAt(0)
                    sendOrderLog()
                } else view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message ?: ""), it.code())
            }, {
                if (it is ConnectException || it is SocketTimeoutException) {
                    view?.errorConnection()
                } else {
                    view?.errorScreen(it.message)
                }
            }, {}))
        } else {
            view?.onSendHistory()
        }
    }

    private fun filterClean(start: Long, end: Long) = listOf<(ParentCleanOffline) -> Boolean>(
        { it.status == Params.STATUS_CLOSED },
        { it.start in start..end }
    )

    private fun filterAc(start: Long, end: Long) = listOf<(ParentAcOffline) -> Boolean>(
        { it.status == Params.STATUS_CLOSED },
        { it.start in start..end }
    )

    private fun filterNb(start: Long, end: Long) = listOf<(ParentNbOffline) -> Boolean>(
        { it.status == Params.STATUS_CLOSED },
        { it.start in start..end }
    )

    private fun filterOther(start: Long, end: Long) = listOf<(ParentOtherOffline) -> Boolean>(
        { it.status == Params.STATUS_CLOSED },
        { it.start in start..end }
    )

    private fun <T> List<T>.filterAll(filters: List<(T) -> Boolean>)
            = filter { item -> filters.all { filter -> filter(item) } }
}