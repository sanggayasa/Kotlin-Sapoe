package com.akarinti.sapoe.data.repository

import android.content.Context
import com.akarinti.sapoe.base.AbstractPrefernces
import com.akarinti.sapoe.model.answer.AnswerScheduled
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.ParentOtherOffline
import com.akarinti.sapoe.objects.PrefKey
import com.akarinti.sapoe.objects.PrefName
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class InprogressRepository @Inject constructor(context: Context, gson: Gson) : AbstractPrefernces(context, gson) {

    override fun getPreferencesGroup(): String = PrefName.PREF_TOKEN_NAME

    var answerScheduled: List<AnswerScheduled>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_INPROGRESS_PARENT, AnswerScheduled::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_INPROGRESS_PARENT)
            else saveData(PrefKey.PREF_KEY_INPROGRESS_PARENT, value)
        }

    var scheduledAc: List<ParentAcOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_S_AC, ParentAcOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_S_AC)
            else saveData(PrefKey.PREF_KEY_OFFLINE_S_AC, value)
        }

    var scheduledNb: List<ParentNbOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_S_NB, ParentNbOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_S_NB)
            else saveData(PrefKey.PREF_KEY_OFFLINE_S_NB, value)
        }

    var scheduledCl: List<ParentCleanOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_S_CL, ParentCleanOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_S_CL)
            else saveData(PrefKey.PREF_KEY_OFFLINE_S_CL, value)
        }
    var answerUnscheduled: List<AnswerScheduled>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_INPROGRESS_PARENT_U, AnswerScheduled::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_INPROGRESS_PARENT_U)
            else saveData(PrefKey.PREF_KEY_INPROGRESS_PARENT_U, value)
        }

    var unscheduledAc: List<ParentAcOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_S_AC_U, ParentAcOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_S_AC_U)
            else saveData(PrefKey.PREF_KEY_OFFLINE_S_AC_U, value)
        }

    var unscheduledNb: List<ParentNbOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_S_NB_U, ParentNbOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_S_NB_U)
            else saveData(PrefKey.PREF_KEY_OFFLINE_S_NB_U, value)
        }

    var unscheduledCl: List<ParentCleanOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_S_CL_U, ParentCleanOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_S_CL_U)
            else saveData(PrefKey.PREF_KEY_OFFLINE_S_CL_U, value)
        }

    var unscheduledMcds: List<ParentOtherOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_U_MCDS, ParentOtherOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_U_MCDS)
            else saveData(PrefKey.PREF_KEY_OFFLINE_U_MCDS, value)
        }

    var unscheduledQc: List<ParentOtherOffline>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_OFFLINE_U_QC, ParentOtherOffline::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_OFFLINE_U_QC)
            else saveData(PrefKey.PREF_KEY_OFFLINE_U_QC, value)
        }
}