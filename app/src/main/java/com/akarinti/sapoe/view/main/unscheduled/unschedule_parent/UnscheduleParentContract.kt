package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.body.SaveAnswerBody

interface UnscheduleParentContract {
    interface View: ErrorView {
        fun onQuestionaireParentAc()
        fun onQuestionaireParentNb()
        fun onQuestionaireParentClean()
        fun onQuestionaireParentMcds()
        fun onQuestionaireParentQc()
        fun onUpdateStatus()
        fun onAnswerSaved()
    }

    interface Presenter{
        fun getQuestionaireParentAC(orderID: String, lat: Double, long: Double)
        fun getQuestionaireParentNB(orderID: String, lat: Double, long: Double)
        fun getQuestionaireParentClean(orderID: String, lat: Double, long: Double)
        fun getQuestionaireParentMcds(orderID: String, lat: Double, long: Double)
        fun getQuestionaireParentQc(orderID: String, lat: Double, long: Double)
        fun updateCompletedUnSchedule(orderID: String, lat: Double, long: Double)
        fun updateStatusSchedule(orderID: String, lat: Double, long: Double)
        fun saveAnswer(input:SaveAnswerBody, isConnected: Boolean, scheduleType: String?, lat: Double, long: Double)
    }
}