package com.akarinti.sapoe.view.main.visit.onprogress

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.ScheduleListResponse
import com.akarinti.sapoe.model.Schedule

interface VisitListContract {
    interface View: ErrorView {
        fun onScheduleSetOffline(data: List<Schedule>?)
        fun onScheduleSetPager(data: ScheduleListResponse)
    }

    interface Presenter{
        fun getSchedule(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int = 1, limit: Int = 10)
        fun getUnsent(): ArrayList<Schedule> = ArrayList()
        fun getOrign(): ArrayList<Schedule> = ArrayList()
    }
}