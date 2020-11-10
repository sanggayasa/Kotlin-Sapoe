package com.akarinti.sapoe.data.repository

import android.content.Context
import com.akarinti.sapoe.base.AbstractPrefernces
import com.akarinti.sapoe.model.ProfileUser
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.PrefKey
import com.akarinti.sapoe.objects.PrefName
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ProfileRepository @Inject constructor(context: Context, gson: Gson) : AbstractPrefernces(context, gson) {

    override fun getPreferencesGroup(): String = PrefName.PREF_TOKEN_NAME

    var profile: ProfileUser? = null
        get() {
            if (null == field) field = getData(PrefKey.PREF_KEY_PROFILE, ProfileUser::class.java)
            return field
        }
        set(value) {
            field = value
            if (null == value) clearData(PrefKey.PREF_KEY_PROFILE)
            else saveData(PrefKey.PREF_KEY_PROFILE, value)
        }

    var orderId: String? = null
        get() {
            if(field == null) field = getString(PrefKey.PREF_KEY_CATEGORY)
            return field
        }
        set(value) {
            if(null == value) {
                field = ""
                saveData(PrefKey.PREF_KEY_CATEGORY, false)
            }
            else {
                field = value
                saveData(PrefKey.PREF_KEY_CATEGORY, value)
            }
        }
    var imageBefore: String? = null
        get() {
            if(field == null) field = getString(Params.IMAGE_BEFORE)
            return field
        }
        set(value) {
            if(null == value) {
                field = ""
                saveData(Params.IMAGE_BEFORE, false)
            }
            else {
                field = value
                saveData(Params.IMAGE_BEFORE, value)
            }
        }
     var imageAfter: String? = null
            get() {
                if(field == null) field = getString(Params.IMAGE_AFTER)
                return field
            }
            set(value) {
                if(null == value) {
                    field = ""
                    saveData(Params.IMAGE_AFTER, false)
                }
                else {
                    field = value
                    saveData(Params.IMAGE_AFTER, value)
                }
            }
}