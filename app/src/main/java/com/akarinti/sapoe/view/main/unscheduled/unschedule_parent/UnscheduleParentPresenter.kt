package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.entity.UnscheduleEntity
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.data.response.*
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.ParentOtherOffline
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class UnscheduleParentPresenter @Inject constructor(
    private val scheduleEntity: ScheduleEntity,
    private val unscheduleEntity: UnscheduleEntity,
    var inprogressRepository: InprogressRepository,
    var archiveRepository: ArchiveRepository,
    private val profileRepository: ProfileRepository
) : BasePresenter<UnscheduleParentContract.View>(), UnscheduleParentContract.Presenter {
    override fun updateCompletedUnSchedule(orderID: String, lat: Double, long: Double) {
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
        )    }


    override fun getQuestionaireParentAC(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.getQuestionnaireOffline(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseAcOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentAcOffline> = inprogressRepository.unscheduledAc?: ArrayList()
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
                    inprogressRepository.unscheduledAc = orignData

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
            category = Params.CATEGORY_UNSCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseNbOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {

                    var orignData: List<ParentNbOffline> = inprogressRepository.unscheduledNb?: ArrayList()
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
                    inprogressRepository.unscheduledNb = orignData

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
            category = Params.CATEGORY_UNSCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseCleanOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentCleanOffline> = inprogressRepository.unscheduledCl?: ArrayList()
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
                    inprogressRepository.unscheduledCl = orignData
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

    override fun getQuestionaireParentMcds(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.getQuestionnaireOffline(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseOtherOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentOtherOffline> = inprogressRepository.unscheduledMcds?: ArrayList()
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
                    inprogressRepository.unscheduledMcds = orignData
                    view?.onQuestionaireParentMcds()
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

    override fun getQuestionaireParentQc(orderID: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.getQuestionnaireOffline(
            orderID = orderID,
            category = Params.CATEGORY_UNSCHEDULED,
            lat = lat,
            long = long)
            .uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(QuestionarieResponseOtherOffline::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    var orignData: List<ParentOtherOffline> = inprogressRepository.unscheduledQc?: ArrayList()
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
                    inprogressRepository.unscheduledQc = orignData
                    view?.onQuestionaireParentQc()
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
        addSubscription(unscheduleEntity.changeUncheduleStatusValidated(orderID, Params.STATUS_INPROGRESS, lat, long).uiSubscribe({
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

    override fun saveAnswer(input: SaveAnswerBody, isConnected: Boolean, scheduleType: String?, lat: Double, long: Double) {
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
            if (scheduleType == "manual") {
                val archive : MutableList<Unschedule> = (archiveRepository.unsentUnSchedule?: listOf()).toMutableList()
                val orign : MutableList<Unschedule> = (archiveRepository.orignUnSchedule?: listOf()).toMutableList()

                archiveRepository.orignUnSchedule?.find { find -> find.id == input.orderId }?.let {
                    archive += it
                    orign.remove(it)
                }
                archiveRepository.unsentUnSchedule = archive
                archiveRepository.orignUnSchedule = orign
            } else {
                val archive : MutableList<Unschedule> = (archiveRepository.unsentUnScheduleAuto?: listOf()).toMutableList()
                val orign : MutableList<Unschedule> = (archiveRepository.orignUnScheduleAuto?: listOf()).toMutableList()

                archiveRepository.orignUnScheduleAuto?.find { find -> find.id == input.orderId }?.let {
                    archive += it
                    orign.remove(it)
                }
                archiveRepository.unsentUnScheduleAuto = archive
                archiveRepository.orignUnScheduleAuto = orign
            }
            view?.onAnswerSaved()
        }
    }
    private fun updateComplete(orderID: String, orderType: String, lat: Double, long: Double) {
        addSubscription(unscheduleEntity.changeUncheduleStatusValidated(orderID, Params.STATUS_COMPLETED, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                when(orderType){
                    Params.TYPE_AC -> {
                        val orignData: MutableList<ParentAcOffline> = (inprogressRepository.unscheduledAc ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.unscheduledAc = orignData
                    }
                    Params.TYPE_NEONBOX -> {
                        val orignData: MutableList<ParentNbOffline> = (inprogressRepository.unscheduledNb ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.unscheduledNb = orignData
                    }
                    Params.TYPE_CLEAN -> {
                        val orignData: MutableList<ParentCleanOffline> = (inprogressRepository.unscheduledCl ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.unscheduledCl = orignData
                    }
                    Params.TYPE_MCDS -> {
                        val orignData: MutableList<ParentOtherOffline> = (inprogressRepository.unscheduledMcds ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.unscheduledMcds = orignData
                    }
                    Params.TYPE_QC -> {
                        val orignData: MutableList<ParentOtherOffline> = (inprogressRepository.unscheduledQc ?: ArrayList()).toMutableList()
                        val data = orignData.find { it.id == orderID }
                        if (null != data) {
                            orignData.remove(data)
                            data.status = Params.STATUS_CLOSED
                            orignData.add(data)
                        }
                        inprogressRepository.unscheduledQc = orignData
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