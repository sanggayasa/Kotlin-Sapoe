package com.akarinti.sapoe.data.repository

import android.content.Context
import com.akarinti.sapoe.base.AbstractPrefernces
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.objects.PrefKey
import com.akarinti.sapoe.objects.PrefName
import com.google.gson.Gson
import javax.inject.Inject

class ArchiveRepository @Inject constructor(context: Context, gson: Gson) : AbstractPrefernces(context, gson) {

    override fun getPreferencesGroup(): String = PrefName.PREF_TOKEN_NAME

    var orignSchedule: List<Schedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_ORIGN_SCHEDULE, Schedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_ORIGN_SCHEDULE)
            else saveData(PrefKey.PREF_KEY_ORIGN_SCHEDULE, value)
        }

    var unsentSchedule: List<Schedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_ARCHIVE_SCHEDULE, Schedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_ARCHIVE_SCHEDULE)
            else saveData(PrefKey.PREF_KEY_ARCHIVE_SCHEDULE, value)
        }
    var orignUnSchedule: List<Unschedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_ORIGN_UNSCHEDULE, Unschedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_ORIGN_UNSCHEDULE)
            else saveData(PrefKey.PREF_KEY_ORIGN_UNSCHEDULE, value)
        }

    var unsentUnSchedule: List<Unschedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_ARCHIVE_UNSCHEDULE, Unschedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_ARCHIVE_UNSCHEDULE)
            else saveData(PrefKey.PREF_KEY_ARCHIVE_UNSCHEDULE, value)
        }
    var orignUnScheduleAuto: List<Unschedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_ORIGN_UNSCHEDULE_AUTO, Unschedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_ORIGN_UNSCHEDULE_AUTO)
            else saveData(PrefKey.PREF_KEY_ORIGN_UNSCHEDULE_AUTO, value)
        }

    var unsentUnScheduleAuto: List<Unschedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_ARCHIVE_UNSCHEDULE_AUTO, Unschedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_ARCHIVE_UNSCHEDULE_AUTO)
            else saveData(PrefKey.PREF_KEY_ARCHIVE_UNSCHEDULE_AUTO, value)
        }
}