package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ProfileUser
import com.google.gson.annotations.SerializedName

class LoginResponse (@SerializedName("data") val data: LoginData? = null): BaseResponse() {
    class LoginData(@SerializedName("detail") val detail: ProfileUser?=null)
}