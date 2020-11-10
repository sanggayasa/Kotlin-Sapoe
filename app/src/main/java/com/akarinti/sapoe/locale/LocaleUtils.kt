package com.akarinti.sapoe.locale

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import com.akarinti.sapoe.event.LanguageChangedEvent
import com.akarinti.sapoe.extension.loadPrefStr
import com.akarinti.sapoe.extension.savePrefStr
import com.akarinti.sapoe.objects.PrefName
import com.akarinti.sapoe.objects.RxBus
import java.util.*

object LocaleUtils {
    var locale: Locale? = null
    private val defaultLocale = Locale("en", "US")

    fun currentLocale(): Locale {
        return this.locale?.let { it }?: kotlin.run {
            this.locale = defaultLocale
            return defaultLocale
        }
    }

    fun onAttach(context: Context?, reset: Boolean): Context? {
        context?.let {
            val locale: String?
            val country: String?
            if (!reset) {
                this.locale?.let { return setLocale(context, it) }
                locale = getPersistedLocale(context)
                country = getPersistedCountry(context)
            } else {
                locale = "en"
                country = "US"
                setPersistedLocale(context, locale, country)
            }
            return setLocale(context, locale, country)
        }
        return null
    }

    fun getPersistedLocale(context: Context): String? {
        return context.loadPrefStr(PrefName.PREF_STATIC_NAME, PrefName.PREF_KEY_LANGUAGE)
    }

    fun getPersistedCountry(context: Context): String? {
        return context.loadPrefStr(PrefName.PREF_STATIC_NAME, PrefName.PREF_KEY_COUNTRY)
    }

    fun setPersistedLocale(context: Context, locale: String, country: String) {
        @Suppress("NAME_SHADOWING") var locale = locale
        @Suppress("NAME_SHADOWING") var country = country
        if (TextUtils.isEmpty(locale)) {
            locale = "en"
            country = "US"
        } else if (TextUtils.isEmpty(country)) {
            country = if (locale == "in") "ID" else "US"
        }
        setPersistedLocale(context, Locale(locale, country))
    }

    fun setPersistedLocale(context: Context, locale: Locale) {
        LocaleUtils.locale = locale
        context.savePrefStr(PrefName.PREF_STATIC_NAME, PrefName.PREF_KEY_LANGUAGE, locale.language)
        context.savePrefStr(PrefName.PREF_STATIC_NAME, PrefName.PREF_KEY_COUNTRY, locale.country)
        RxBus.publish(LanguageChangedEvent(locale))
    }

    /**
     * Set the app's localeSpec1 to the one specified by the given String.
     *
     * @param context
     * @param localeSpec1 a localeSpec1 specification as used for Android resources (NOTE: does not
     * support countrySpec1 and variant codes so far); the special string "system" sets
     * the localeSpec1 to the localeSpec1 specified in system settings
     * @return
     */
    fun setLocale(context: Context, localeSpec1: String?, countrySpec1: String?): Context {
        var localeSpec = localeSpec1
        var countrySpec = countrySpec1
        val locale: Locale
        if (localeSpec == "system") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().configuration.locales.get(0)
            } else {
                @Suppress("DEPRECATION")
                locale = Resources.getSystem().configuration.locale
            }
        } else {
            if (TextUtils.isEmpty(localeSpec)) {
                localeSpec = "en"
                countrySpec = "US"
                setPersistedLocale(context, localeSpec, countrySpec)
            } else if (TextUtils.isEmpty(countrySpec)) {
                countrySpec = if (localeSpec == "in") "ID" else "US"
            }
            locale = Locale(localeSpec, countrySpec)
        }
        LocaleUtils.locale = locale
        return setLocale(context, locale)
    }

    private fun setLocale(context: Context, locale: Locale): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

}
