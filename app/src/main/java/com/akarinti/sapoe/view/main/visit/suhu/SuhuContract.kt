package com.akarinti.sapoe.view.main.visit.suhu

import com.akarinti.sapoe.base.ErrorView


interface SuhuContract {
    interface View: ErrorView {
        fun gotoNextPage()
    }

    interface Presenter{
        fun loadSuhu()
        fun createSuhuClean(category: String, orderId: String, orderType: String, picture: String, temperature: String)
    }
}