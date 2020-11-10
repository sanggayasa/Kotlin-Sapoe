package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Suhu
import com.google.gson.annotations.SerializedName


class SuhuResponse (@SerializedName("data") val data: SuhuData? = null): BaseResponse() {
    class SuhuData(@SerializedName("detail") val detail: Suhu?=null)
}