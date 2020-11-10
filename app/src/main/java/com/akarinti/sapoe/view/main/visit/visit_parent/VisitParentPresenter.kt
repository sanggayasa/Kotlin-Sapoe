package com.akarinti.sapoe.view.main.visit.visit_parent

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.data.response.QuestionarieResponseAcOffline
import com.akarinti.sapoe.data.response.QuestionarieResponseCleanOffline
import com.akarinti.sapoe.data.response.QuestionarieResponseNbOffline
import com.akarinti.sapoe.data.response.SuhuResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class VisitParentPresenter @Inject constructor(
    private val scheduleEntity: ScheduleEntity,
    var inprogressRepository: InprogressRepository,
    var archiveRepository: ArchiveRepository,
    private val profileRepository: ProfileRepository
) : BasePresenter<VisitParentContract.View>(), VisitParentContract.Presenter {

    override fun getQuestionaireParentAC(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.getQuestionnaireOffline(
            orderID = orderID,
            category = Params.CATEGORY_SCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseAcOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentAcOffline> = inprogressRepository.scheduledAc?: ArrayList()
                    wrapper.response.data.start = Calendar.getInstance().timeInMillis / 1000L
                    wrapper.response.data.agentName = profileRepository.profile!!.name
                    wrapper.response.data.agentID = profileRepository.profile!!.id
//                    orignData += wrapper.response.data
                    var temp = wrapper.response.data
                    temp.formList = wrapper.response.data.formList?.sortedBy { item -> item.index }
                    for (i in (temp.formList?: listOf()).indices) {
                        temp.formList?.get(i)?.questionList = wrapper.response.data.formList?.get(i)?.questionList?.sortedBy { item -> item.index }
                    }
                    temp.itemList = wrapper.response.data.itemList?.sortedBy { item -> item.index }
                    for (i in (temp.itemList?: listOf()).indices) {
                        temp.itemList?.get(i)?.formList = wrapper.response.data.itemList?.get(i)?.formList?.sortedBy { item -> item.index }
                        for (j in (temp.itemList?.get(i)?.formList?: listOf()).indices) {
                            temp.itemList?.get(i)?.formList?.get(j)?.questionList = wrapper.response.data.itemList?.get(i)?.formList?.get(j)?.questionList?.sortedBy { item -> item.index }
                        }
                    }
                    orignData += temp
                    inprogressRepository.scheduledAc = orignData

                    view?.onQuestionaireParentAc()
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

    override fun getQuestionaireParentNB(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.getQuestionnaireOffline(
            orderID = orderID,
            category = Params.CATEGORY_SCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseNbOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentNbOffline> = inprogressRepository.scheduledNb?: ArrayList()
                    wrapper.response.data.start = Calendar.getInstance().timeInMillis / 1000L
                    wrapper.response.data.agentName = profileRepository.profile!!.name
                    wrapper.response.data.agentID = profileRepository.profile!!.id
//                    orignData += wrapper.response.data
                    var temp = wrapper.response.data
                    temp.formList = wrapper.response.data.formList?.sortedBy { item -> item.index }
                    for (i in (temp.formList?: listOf()).indices) {
                        temp.formList?.get(i)?.questionList = wrapper.response.data.formList?.get(i)?.questionList?.sortedBy { item -> item.index }
                    }
                    temp.itemList = wrapper.response.data.itemList?.sortedBy { item -> item.index }
                    for (i in (temp.itemList?: listOf()).indices) {
                        temp.itemList?.get(i)?.formList = wrapper.response.data.itemList?.get(i)?.formList?.sortedBy { item -> item.index }
                        for (j in (temp.itemList?.get(i)?.formList?: listOf()).indices) {
                            temp.itemList?.get(i)?.formList?.get(j)?.questionList = wrapper.response.data.itemList?.get(i)?.formList?.get(j)?.questionList?.sortedBy { item -> item.index }
                        }
                    }
                    orignData += temp
                    inprogressRepository.scheduledNb = orignData

                    view?.onQuestionaireParentNb()
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

    override fun getQuestionaireParentClean(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.getQuestionnaireOffline(
            orderID = orderID,
            category = Params.CATEGORY_SCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseCleanOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentCleanOffline> = inprogressRepository.scheduledCl?: ArrayList()
                    wrapper.response.data.start = Calendar.getInstance().timeInMillis / 1000L
                    wrapper.response.data.agentName = profileRepository.profile!!.name
                    wrapper.response.data.agentID = profileRepository.profile!!.id
//                    orignData += wrapper.response.data
                    var temp = wrapper.response.data
                    temp.formList = wrapper.response.data.formList?.sortedBy { item -> item.index }
                    for (i in (temp.formList?: listOf()).indices) {
                        temp.formList?.get(i)?.questionList = wrapper.response.data.formList?.get(i)?.questionList?.sortedBy { item -> item.index }
                    }
                    temp.itemList = wrapper.response.data.itemList?.sortedBy { item -> item.index }
                    for (i in (temp.itemList?: listOf()).indices) {
                        temp.itemList?.get(i)?.formList = wrapper.response.data.itemList?.get(i)?.formList?.sortedBy { item -> item.index }
                        for (j in (temp.itemList?.get(i)?.formList?: listOf()).indices) {
                            temp.itemList?.get(i)?.formList?.get(j)?.questionList = wrapper.response.data.itemList?.get(i)?.formList?.get(j)?.questionList?.sortedBy { item -> item.index }
                        }
                    }
                    orignData += temp
                    inprogressRepository.scheduledCl = orignData

                    view?.onQuestionaireParentClean()
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

    override fun updateStatusSchedule(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.changeScheduleStatusValidated(orderID, Params.STATUS_INPROGRESS, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                view?.onUpdateStatus()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message ?: ""), it.code())
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

    override fun updateCompletedSchedule(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.changeScheduleStatusValidated(orderID, Params.STATUS_COMPLETED, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                view?.onUpdateStatus()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message ?: ""), it.code())
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

    override fun saveAnswer(input: SaveAnswerBody, isConnected: Boolean, lat: Double, long: Double) {
        if (isConnected) {
            addSubscription(scheduleEntity.submitAnswer(input).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
                if (it.code() == HttpURLConnection.HTTP_OK) {
                    if (input.orderType == "clean")
                        sendTemperature(input.category, input.orderId, input.orderType, input.suhuPicture, input.suhuAnswer, lat, long)
                    else
                        updateComplete(input.orderId, input.orderType, lat, long)

                } else {
                    view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message ?: ""), it.code())
                }
            }, {
                if (it is ConnectException || it is SocketTimeoutException) {
                    view?.errorConnection()
                } else {
                    view?.errorScreen(it.message)
                }
            }, {})
            )
        } else {
            val archive : MutableList<Schedule> = (archiveRepository.unsentSchedule?: listOf()).toMutableList()
            val orign : MutableList<Schedule> = (archiveRepository.orignSchedule?: listOf()).toMutableList()

            archiveRepository.orignSchedule?.find { find -> find.id == input.orderId }?.let {
                archive += it
                orign.remove(it)
            }
            archiveRepository.unsentSchedule = archive
            archiveRepository.orignSchedule = orign

            view?.onAnswerSaved()
        }
    }

    private fun updateComplete(orderID: String, orderType: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.changeScheduleStatusValidated(orderID, Params.STATUS_COMPLETED, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                when(orderType){
                    Params.TYPE_AC -> {
                        val orignData: MutableList<ParentAcOffline> = (inprogressRepository.scheduledAc ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.scheduledAc = orignData
                    }
                    Params.TYPE_NEONBOX -> {
                        val orignData: MutableList<ParentNbOffline> = (inprogressRepository.scheduledNb ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.scheduledNb = orignData
                    }
                    Params.TYPE_CLEAN -> {
                        val orignData: MutableList<ParentCleanOffline> = (inprogressRepository.scheduledCl ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.scheduledCl = orignData
                    }
                }
                view?.onAnswerSaved()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(wrapper.response?.meta?.message ?: ""), it.code())
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

    private fun sendTemperature(category: String, orderId: String, orderType: String, picture: String, temperature: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.createSuhu(category, orderId, orderType, picture, temperature).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SuhuResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                updateComplete(orderId, orderType, lat, long)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
            }
        }, {view?.errorScreen(it.message)}, {}))
    }
}