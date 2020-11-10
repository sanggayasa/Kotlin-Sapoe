package com.akarinti.sapoe.view.main.visit.visit_parent

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.body.SaveAnswerBody

interface VisitParentContract {
    interface View: ErrorView {
        fun onQuestionaireParentAc()
        fun onQuestionaireParentNb()
        fun onQuestionaireParentClean()
        fun onUpdateStatus()
        fun onAnswerSaved()
    }

    interface Presenter{
        fun getQuestionaireParentAC(orderID: String, lat: Double, long: Double)
        fun getQuestionaireParentNB(orderID: String, lat: Double, long: Double)
        fun getQuestionaireParentClean(orderID: String, lat: Double, long: Double)
        fun updateStatusSchedule(orderID: String, lat: Double, long: Double)
        fun updateCompletedSchedule(orderID: String, lat: Double, long: Double)
        fun saveAnswer(input:SaveAnswerBody, isConnected: Boolean, lat: Double, long: Double)
    }
}