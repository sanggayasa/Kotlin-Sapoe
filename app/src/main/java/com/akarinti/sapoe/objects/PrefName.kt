package com.akarinti.sapoe.objects

import androidx.annotation.StringDef
import com.akarinti.sapoe.BuildConfig

object PrefName {
    const val PREF_STATIC_NAME = "pref_static"
    const val PREF_KEY_LANGUAGE = "p_lang"
    const val PREF_KEY_COUNTRY = "p_country"
    const val PREF_KEY_USER = "p_user"
    const val PREF_TOKEN_NAME = "Sapoe" + BuildConfig.FLAVOR

    @StringDef(PREF_STATIC_NAME)
    annotation class Name

    @StringDef(PREF_KEY_COUNTRY, PREF_KEY_LANGUAGE)
    annotation class Key
}