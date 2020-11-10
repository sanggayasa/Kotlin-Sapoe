package com.akarinti.sapoe.extension

import android.text.Html
import android.text.Spanned
import android.util.Patterns
import kotlin.math.round

var NUM_PATTERN = Regex("^([0-9])*$")
var SYMBOL = Regex("[^a-zA-Z0-9]")

fun CharSequence?.isNumeric(): Boolean {
    return this != null && this.matches(NUM_PATTERN)
}

fun CharSequence?.isValidEmail(): Boolean {
    return this != null && this.matches(Patterns.EMAIL_ADDRESS.toRegex())
}

fun CharSequence?.isValidPhone(): Boolean {
    return this != null && this.matches(Patterns.EMAIL_ADDRESS.toRegex())
}

fun CharSequence?.isMinimum(num: Int): Boolean {
    return this != null && this.length >= num
}

fun CharSequence?.isMaximum(num: Int): Boolean {
    return this != null && this.length <= num
}

fun CharSequence?.allCharacterSame(): Boolean {
    if(this == null) return true
    for (i in this.indices) {
        if(this[i] != this[0]) return false
    }
    return true
}

fun String?.toHtml(): Spanned? {
    var result: Spanned? = null
    if (this != null) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(this)
        }
    }
    return result
}

fun String?.getUrlFileName(): String {
    if (null != this) {
        val slashIndex = this.lastIndexOf('/')
        var queryStringIndex = this.lastIndexOf('?', slashIndex)
        return if (queryStringIndex == -1) {
            this.substring(slashIndex + 1)
        } else this.substring(slashIndex + 1, queryStringIndex)
    }
    return ""
}

fun String?.getNameExtension(): String {
    if (null != this) {
        val dotIndex = this.lastIndexOf('.')
        return if(dotIndex == -1) ""
        else this.substring(dotIndex)
    }
    return ""
}

fun String.formatSpanned(): Spanned {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(this)
    }
}

fun Double.formatDigit(digits: Int): String = "%.${digits}f".format(this)

fun Double.rounded(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun CharSequence.isValidQuantity(): Boolean {
    return try {
        if (this.toString().isEmpty()) return false
        this.toString().toFloatOrNull()?.let {
            it in 0.5 .. 99.0 && (it % 0.5 == 0.0)
        }?: false
    } catch (e: Exception) {
        false
    }
}