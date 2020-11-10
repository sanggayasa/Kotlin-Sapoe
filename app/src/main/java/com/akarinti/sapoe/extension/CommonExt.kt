package com.akarinti.sapoe.extension

import android.content.Context
import com.akarinti.sapoe.locale.LocaleUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ridwan on 03/03/2018.
 */
fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss",
                  locale: Locale = LocaleUtils.locale!!
): Date {
    return try {
        SimpleDateFormat(pattern, locale).parse(this)
    } catch (e: Exception) {
        Date()
    }
}

fun String.dateFormat(input: String = "yyyy-MM-dd HH:mm:ss",
                      inputLocale: Locale = java.util.Locale.US,
                      output: String = "dd MMM yyyy, HH:mm:ss",
                      outputLocale: Locale = LocaleUtils.locale!!
): String {
    return this.toDate(input, inputLocale).format(output, outputLocale)
}

fun String.dateFormat(input: String = "yyyy-MM-dd HH:mm:ss",
                      output: String = "dd MMM yyyy, HH:mm:ss"): String {
    return this.toDate(input, LocaleUtils.locale!!).format(output, LocaleUtils.locale!!)
}

fun String.dateFormat(dateFormat: DateFormat,
                      input: String = "yyyy-MM-dd HH:mm:ss",
                      inputLocale: Locale): String {
    return this.toDate(input, inputLocale).format(dateFormat)
}

fun String.dateFormat(dateFormat: DateFormat,
                      input: String = "yyyy-MM-dd HH:mm:ss"): String {
    return this.toDate(input, LocaleUtils.locale!!).format(dateFormat)
}

fun String?.nonNull(): String {
    if (this != null) {
        return this
    }
    return ""
}

fun Date.format(pattern: String = "dd MMM yyyy, HH:mm:ss",
                locale: Locale = Locale.getDefault()): String {
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(this)
}

fun Date.format(pattern: String = "dd MMM yyyy, HH:mm:ss",
                locale: Locale = Locale.getDefault(),
                timezone: TimeZone): String {
    val sdf = SimpleDateFormat(pattern, locale)
    sdf.timeZone = timezone
    return sdf.format(this)
}

fun Date.format(dateFormat: DateFormat): String {
    return dateFormat.format(this)
}

fun Long.dateFormat(pattern: String = "dd MMM yyyy, HH:mm:ss",
                    locale: Locale): String {
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(Date(this))
}

fun Long.dateFormat(pattern: String = "dd MMM yyyy, HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern, LocaleUtils.locale)
    return sdf.format(Date(this))
}

fun Long.dateFormat(dateFormat: DateFormat): String {
    return dateFormat.format(Date(this))
}

fun Context.loadPrefStr(prefName: String, prefKey: String): String? {
    val sharedPref = this.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    return sharedPref.getString(prefKey, "")
}

fun Context.savePrefStr(prefName: String, prefKey: String, value: String) {
    val sharedPref = this.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    val prefEditor = sharedPref.edit()
    prefEditor.putString(prefKey, value)
    prefEditor.apply()
}