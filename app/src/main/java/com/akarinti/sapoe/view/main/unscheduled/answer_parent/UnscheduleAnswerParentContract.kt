package com.akarinti.sapoe.view.main.unscheduled.answer_parent

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.ParentOtherOffline

interface UnscheduleAnswerParentContract {
    interface View: ErrorView {
        fun onQuestionaireParentAc(data: ParentAcOffline?)
        fun onQuestionaireParentNb(data: ParentNbOffline?)
        fun onQuestionaireParentClean(data: ParentCleanOffline?)
        fun onQuestionaireParentMcds(data: ParentOtherOffline?)
        fun onQuestionaireParentQc(data: ParentOtherOffline?)
    }
    interface Presenter {
        fun getQuestionaireParentAC(orderID: String)
        fun getQuestionaireParentNB(orderID: String)
        fun getQuestionaireParentClean(orderID: String)
        fun getQuestionaireParentMcds(orderID: String)
        fun getQuestionaireParentQc(orderID: String)
    }
}