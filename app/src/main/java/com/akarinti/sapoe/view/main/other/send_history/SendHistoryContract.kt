package com.akarinti.sapoe.view.main.other.send_history

import com.akarinti.sapoe.base.ErrorView

interface SendHistoryContract {
    interface View : ErrorView {
        fun onSendHistory()
    }
    interface Presenter {
        fun sendHistory(start: Long, end: Long)
    }
}