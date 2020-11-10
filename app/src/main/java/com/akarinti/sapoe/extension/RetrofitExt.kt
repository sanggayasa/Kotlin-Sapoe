package com.akarinti.sapoe.extension

import com.akarinti.sapoe.data.response.ResponseWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

fun Response<*>.getErrorMessage(): String? {
    var jsonObject: JSONObject? = null
    try {
        jsonObject = JSONObject(this.raw().body()?.string())
    } catch (e: Exception) {
    }
    return jsonObject.getErrorMessage(this.message())
}

fun <T> Response<ResponseBody>.convertResponse(typeToken: TypeToken<T>): ResponseWrapper<T> {
    val string = if (this.code() == 200) this.body()?.string() ?: "" else this.errorBody()?.string()?: ""
    var jsonObject: JSONObject? = null
    var obj: T? = null
    try {
        jsonObject = JSONObject(string)
    } catch (e: Exception) { }
    try {
        obj = Gson().fromJson(string, typeToken.type)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ResponseWrapper(jsonObject, obj)
}