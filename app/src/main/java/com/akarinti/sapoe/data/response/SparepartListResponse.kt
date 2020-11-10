package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.SparepartList
import com.google.gson.annotations.SerializedName


class SparepartListResponse (@SerializedName("data") val data: createSparepartList? = null): BaseResponse() {
    class createSparepartList(@SerializedName("detail") val detail: SparepartList?=null)
}