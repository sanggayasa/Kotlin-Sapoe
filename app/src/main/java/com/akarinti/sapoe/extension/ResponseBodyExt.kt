package com.akarinti.sapoe.extension

import com.google.gson.Gson
import org.json.JSONObject

fun String.getErrorSchemaMessage() : String? {
    var message: String? = null
    var jsonObject: JSONObject? = null
    try {
        jsonObject = JSONObject(this)
    } catch (e: Exception) {
    }
    return jsonObject.getErrorMessage("")
}

fun String.getErrorMessage(default: String) : String? {
    var message: String? = null
    var jsonObject: JSONObject? = null
    try {
        jsonObject = JSONObject(this)
    } catch (e: Exception) {
    }
    return jsonObject.getErrorMessage(default)
}

fun JSONObject?.getErrorMessage(default: String): String? {
    var message: String? = null
    if (message.isNullOrEmpty()) {
        try {
            this?.let {
                message = it.getJSONObject("meta").getString("message")
            }
        } catch (e: Exception) {
        }
    }
    if (message.isNullOrEmpty()) {
        message = default
    }
    return message
}

fun JSONObject?.getErrorCode(): Int? {
    var message: Int? = null
    try {
        this?.let {
            message = it.getJSONObject("meta").getInt("code")
        }
    } catch (e: Exception) {
    }
    return message
}

fun String.toJsonObject(): JSONObject? {
    try {
        return JSONObject(this)
    } catch (e: Exception) {
    }
    return null
}

fun <T> String.convertJson(c: Class<T>): T? {
    try {
        return Gson().fromJson(this, c)
    } catch (_: Exception) {
    }
    return null
}