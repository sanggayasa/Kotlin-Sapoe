package com.akarinti.sapoe.view.main.unscheduled.unsent

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.Unschedule

interface UnscheduledPendingContract {
    interface View: ErrorView
    interface Presenter {
        fun manualList(): ArrayList<Unschedule> = ArrayList()
        fun autoList(): ArrayList<Unschedule>? = ArrayList()
    }
}