package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Unschedule
import com.google.gson.annotations.SerializedName

class CreateUnscheduleResponse (@SerializedName("data") val data: CreateUnscheduleData? = null): BaseResponse() {
    class CreateUnscheduleData(@SerializedName("detail") val detail: Unschedule?=null)
}