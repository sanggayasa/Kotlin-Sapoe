package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ProfileUser
import com.google.gson.annotations.SerializedName

class LogoutResponse (@SerializedName("data") val data: LogoutData? = null): BaseResponse() {
    class LogoutData(@SerializedName("detail") val detail: ProfileUser?=null)
}