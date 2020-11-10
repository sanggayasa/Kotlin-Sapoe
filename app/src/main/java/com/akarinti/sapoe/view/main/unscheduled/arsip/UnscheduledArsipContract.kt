package com.akarinti.sapoe.view.main.unscheduled.arsip

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.UnscheduleListResponse

interface UnscheduledArsipContract {
    interface View: ErrorView {
        fun gotoCompleteUnschedulePager(data: UnscheduleListResponse)
        fun gotoCompleteUnscheduleManualPager(data: UnscheduleListResponse)
    }

    interface Presenter{
        fun getCompleteUnschedule(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int = 1, limit: Int = 10)
        fun getCompleteUnscheduleManual(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int = 1, limit: Int = 10)
    }
}