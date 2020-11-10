package com.akarinti.sapoe.view.main

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.data.entity.LocationEntity
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.entity.UnscheduleEntity
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.data.response.SuhuResponse
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

class MainPresenter @Inject constructor(
    private val locationEntity: LocationEntity,
    private val scheduleEntity: ScheduleEntity,
    private val unscheduleEntity: UnscheduleEntity,
    var archiveRepository: ArchiveRepository,
    var inprogressRepository: InprogressRepository)
    : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun sendLocationTracking(lat: Double, long: Double, deviceName: String, imei: String) {
        addSubscription(locationEntity.sendLocationTracking(lat, long, deviceName, imei).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() != HttpURLConnection.HTTP_OK) {
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

    override fun saveAnswer(input: SaveAnswerBody, lat: Double, long: Double) {
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
    }
    override fun saveAnswerUnsch(input: SaveAnswerBody, lat: Double, long: Double) {
        addSubscription(scheduleEntity.submitAnswer(input).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                if (input.orderType == "clean")
                    sendTemperatureManual(input.category, input.orderId, input.orderType, input.suhuPicture, input.suhuAnswer, lat, long)
                else
                    updateManualComplete(input.orderId, input.orderType, lat, long)
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
    override fun saveAnswerUnschAuto(input: SaveAnswerBody, lat: Double, long: Double) {
        addSubscription(scheduleEntity.submitAnswer(input).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                if (input.orderType == "clean")
                    sendTemperatureAuto(input.category, input.orderId, input.orderType, input.suhuPicture, input.suhuAnswer, lat, long)
                else
                    updateAutoComplete(input.orderId, input.orderType, lat, long)
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
                val unsentData = (archiveRepository.unsentSchedule?: listOf()).toMutableList()
                unsentData.removeAt(0)
                archiveRepository.unsentSchedule = unsentData

                view?.onSentSchedule(unsentData)
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
    private fun updateManualComplete(orderID: String, orderType: String, lat: Double, long: Double) {
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
                val unsentManualData = (archiveRepository.unsentUnSchedule?: listOf()).toMutableList()
                unsentManualData.removeAt(0)
                archiveRepository.unsentUnSchedule = unsentManualData

                view?.onSentScheduleManual(unsentManualData)
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
    private fun updateAutoComplete(orderID: String, orderType: String, lat: Double, long: Double) {
        addSubscription(unscheduleEntity.changeUncheduleStatusValidated(orderID, Params.STATUS_COMPLETED, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                when(orderType){
                    Params.TYPE_AC -> {
                        val orignData: MutableList<ParentAcOffline> = (inprogressRepository.unscheduledAc ?: ArrayList()).toMutableList()
//                        orignData.dropWhile { find -> find.id == orderID }
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
                val unsentAutoData = (archiveRepository.unsentUnScheduleAuto?: listOf()).toMutableList()
                unsentAutoData.removeAt(0)
                archiveRepository.unsentUnScheduleAuto = unsentAutoData

                view?.onSentScheduleAuto(unsentAutoData)
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

    private fun sendTemperatureManual(category: String, orderId: String, orderType: String, picture: String, temperature: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.createSuhu(category, orderId, orderType, picture, temperature).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SuhuResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                updateManualComplete(orderId, orderType, lat, long)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
            }
        }, {view?.errorScreen(it.message)}, {}))
    }

    private fun sendTemperatureAuto(category: String, orderId: String, orderType: String, picture: String, temperature: String, lat: Double, long: Double) {
        addSubscription(scheduleEntity.createSuhu(category, orderId, orderType, picture, temperature).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SuhuResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                updateAutoComplete(orderId, orderType, lat, long)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
            }
        }, {view?.errorScreen(it.message)}, {}))
    }
}