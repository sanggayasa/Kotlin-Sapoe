package com.akarinti.sapoe.data.repository

import android.content.Context
import com.akarinti.sapoe.base.AbstractPrefernces
import com.akarinti.sapoe.model.answer.ChildSchedule
import com.akarinti.sapoe.objects.PrefKey
import com.akarinti.sapoe.objects.PrefName
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class ChildbuttonRepository @Inject constructor(context: Context, gson: Gson) : AbstractPrefernces(context, gson) {

    override fun getPreferencesGroup(): String = PrefName.PREF_TOKEN_NAME

    var childScheduled: List<ChildSchedule>? = null
        get() {
            if (null == field) field = getDataList(PrefKey.PREF_KEY_INPROGRESS_CHILD, ChildSchedule::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_INPROGRESS_CHILD)
            else saveData(PrefKey.PREF_KEY_INPROGRESS_CHILD, value)
        }
}