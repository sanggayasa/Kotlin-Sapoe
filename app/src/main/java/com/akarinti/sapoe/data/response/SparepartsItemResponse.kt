package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.Spareparts
import com.google.gson.annotations.SerializedName


data class SparepartsItemResponse(
    @SerializedName("data") val data: SparepartsData? = null
) : BaseResponse() {
    data class SparepartsData(
        @SerializedName("list") val list: List<Spareparts>? = null
    )
}