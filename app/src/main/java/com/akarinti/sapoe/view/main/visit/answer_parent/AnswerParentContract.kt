package com.akarinti.sapoe.view.main.visit.answer_parent

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline

interface AnswerParentContract {
    interface View: ErrorView {
        fun onQuestionaireParentAc(data: ParentAcOffline?)
        fun onQuestionaireParentNb(data: ParentNbOffline?)
        fun onQuestionaireParentClean(data: ParentCleanOffline?)
    }
    interface Presenter {
        fun getQuestionaireParentAC(orderID: String)
        fun getQuestionaireParentNB(orderID: String)
        fun getQuestionaireParentClean(orderID: String)
    }
}