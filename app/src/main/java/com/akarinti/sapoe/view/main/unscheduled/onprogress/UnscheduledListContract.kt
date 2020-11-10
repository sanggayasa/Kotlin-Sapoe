package com.akarinti.sapoe.view.main.unscheduled.onprogress

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.UnscheduleListResponse
import com.akarinti.sapoe.model.Unschedule

interface UnscheduledListContract {
    interface View: ErrorView {
        fun onUnScheduleSetAuto(data: List<Unschedule>?)
        fun onUnScheduleSetManual(data: List<Unschedule>?)
        fun onUnScheduleSetAutoPager(data: UnscheduleListResponse)
        fun onUnScheduleSetManualPager(data: UnscheduleListResponse)
    }

    interface Presenter{
        fun getUnScheduleAuto(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int = 1, limit: Int = 10)
        fun getUnScheduleManual(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int = 1, limit: Int = 10)
        fun getUnsentManual(): ArrayList<Unschedule> = ArrayList()
        fun getUnsentAuto(): ArrayList<Unschedule> = ArrayList()
        fun getOrignManual(): ArrayList<Unschedule> = ArrayList()
        fun getOrignAuto(): ArrayList<Unschedule> = ArrayList()
    }
}