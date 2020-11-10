package com.akarinti.sapoe.view.main.visit.arsip

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.ScheduleListResponse

interface VisitArsipContract {
    interface View: ErrorView {
        fun onCompletedSchedulePager(data: ScheduleListResponse)
    }
    interface Presenter{
        fun getCompletedSchedule(page: Int = 1, limit: Int = 10)
    }
}