package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.MenuCount
import com.google.gson.annotations.SerializedName

data class OrderTicketCountResponse(@SerializedName("data") val data: Data? = null) : BaseResponse() {
    data class Data(@SerializedName("detail") val detail: MenuCount? = null)
}