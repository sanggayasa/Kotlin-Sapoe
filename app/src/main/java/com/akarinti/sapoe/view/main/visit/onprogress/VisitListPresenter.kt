package com.akarinti.sapoe.view.main.visit.onprogress

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.data.response.ScheduleListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class VisitListPresenter @Inject constructor(
    private val scheduleEntity: ScheduleEntity,
    val archiveRepository: ArchiveRepository,
    private val profileRepository: ProfileRepository
) : BasePresenter<VisitListContract.View>(), VisitListContract.Presenter {
    override fun getSchedule(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int, limit: Int) {
        if (isNetworkAvailable) {
            addSubscription(scheduleEntity.getListScheduleDistance(
                    "",
                    "${Params.STATUS_OPEN},${Params.STATUS_INPROGRESS}",
                    lat, long, page, limit
                ).uiSubscribe({
                    val wrapper = it.convertResponse(TypeToken.get(ScheduleListResponse::class.java))
                    if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                        view?.onScheduleSetPager(wrapper.response)
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
        } else {
            view?.onScheduleSetOffline(archiveRepository.orignSchedule)
        }
    }

    override fun getUnsent(): ArrayList<Schedule> {
        return ArrayList((archiveRepository.unsentSchedule?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }

    override fun getOrign(): ArrayList<Schedule> {
        return ArrayList((archiveRepository.orignSchedule?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }
}