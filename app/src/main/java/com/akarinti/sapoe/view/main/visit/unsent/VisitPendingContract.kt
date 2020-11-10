package com.akarinti.sapoe.view.main.visit.unsent

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.Schedule

interface VisitPendingContract {
    interface View: ErrorView
    interface Presenter {
        fun unsentList(): ArrayList<Schedule> = ArrayList()
    }
}