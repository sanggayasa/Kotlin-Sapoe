package com.akarinti.sapoe.data.header

import android.content.Context
import com.akarinti.sapoe.base.AbstractPrefernces
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.model.Token
import com.akarinti.sapoe.objects.PrefKey
import com.akarinti.sapoe.objects.PrefName
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class HeaderManager @Inject constructor(val profileRepository: ProfileRepository,
                                             val context: Context,
                                             gson: Gson)
    : AbstractPrefernces(context, gson) {

    init {
        initFirebaseToken()
    }

    var fcmToken: String? = null

    var authToken: Token? = null
    get() {
        if(null == field) field = getData(PrefKey.PREF_KEY_AUTH_TOKEN, Token::class.java)
        return field
    }
    set(value) {
        field = value
        if(null == value) clearData(PrefKey.PREF_KEY_AUTH_TOKEN)
        else saveData(PrefKey.PREF_KEY_AUTH_TOKEN, value)
    }

    var accessToken: Token? = null
        get() {
            if(null == field) field = getData(PrefKey.PREF_KEY_ACCESS_TOKEN, Token::class.java)
            return field
        }
        set(value) {
            field = value
            if(null == value) clearData(PrefKey.PREF_KEY_ACCESS_TOKEN)
            else saveData(PrefKey.PREF_KEY_ACCESS_TOKEN, value)
        }

    var appList: ArrayList<String> = arrayListOf()

    override fun getPreferencesGroup(): String = PrefName.PREF_TOKEN_NAME

    fun hasToken(): Boolean {
        return null != getToken()
    }

    fun isLoggedIn(): Boolean {
        return (null != accessToken && null != profileRepository.profile)
    }

    fun getToken(): Token? {
        return accessToken?:authToken
    }

    fun getBearerToken(): String {
        return "Bearer ${getToken()?.accessToken}"
    }

    fun clearToken() {
        accessToken = null
        authToken = null
    }
    fun logout() {
        accessToken = null
        profileRepository.profile = null
    }

    private fun initFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful) fcmToken = it.result?.token
        }
    }
}

