package com.akarinti.sapoe.view.main.unscheduled

import com.akarinti.sapoe.base.ErrorView

interface UnscheduledContract {
    interface View: ErrorView {
        fun gotoNextPage()

    }

    interface Presenter{
        fun getVisit()
    }
}