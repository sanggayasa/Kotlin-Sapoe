package com.akarinti.sapoe.view.main.unscheduled.onprogress

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.UnscheduleEntity
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.data.response.UnscheduleListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class UnscheduledListPresenter @Inject constructor(
    private val unscheduleEntity: UnscheduleEntity,
    val archiveRepository: ArchiveRepository,
    private val profileRepository: ProfileRepository
) : BasePresenter<UnscheduledListContract.View>(), UnscheduledListContract.Presenter {
    override fun getUnScheduleManual(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int, limit: Int) {
        if (isNetworkAvailable) {
        addSubscription(
            unscheduleEntity.getListUnscheduleDistance(
                "",
                Params.UNSCHEDULE_MANUAL,
                "${Params.STATUS_OPEN},${Params.STATUS_INPROGRESS}",
                lat,
                long,
                page,
                limit
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(UnscheduleListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onUnScheduleSetManualPager(wrapper.response)
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
        ) } else {
            view?.onUnScheduleSetManual(archiveRepository.orignUnSchedule)
        }
        }

    override fun getUnScheduleAuto(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int, limit: Int) {
        if (isNetworkAvailable) {
        addSubscription(
            unscheduleEntity.getListUnscheduleDistance(
                "",
                Params.UNSCHEDULE_AUTO,
                "${Params.STATUS_OPEN},${Params.STATUS_INPROGRESS}",
                lat,
                long,
                page,
                limit
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(UnscheduleListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onUnScheduleSetAutoPager(wrapper.response)
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
        ) } else {
            view?.onUnScheduleSetAuto(archiveRepository.orignUnScheduleAuto)
        }
    }

    override fun getUnsentManual(): ArrayList<Unschedule> {
        return ArrayList((archiveRepository.unsentUnSchedule?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }

    override fun getUnsentAuto(): ArrayList<Unschedule> {
        return ArrayList((archiveRepository.unsentUnScheduleAuto?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }

    override fun getOrignManual(): ArrayList<Unschedule> {
        return ArrayList((archiveRepository.orignUnSchedule?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }

    override fun getOrignAuto(): ArrayList<Unschedule> {
        return ArrayList((archiveRepository.orignUnScheduleAuto?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }
}