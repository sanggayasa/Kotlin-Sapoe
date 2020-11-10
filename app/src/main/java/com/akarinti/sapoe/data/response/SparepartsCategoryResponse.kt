package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.Category
import com.akarinti.sapoe.model.ticket.Spareparts
import com.google.gson.annotations.SerializedName


data class SparepartsCategoryResponse(
    @SerializedName("data") val data: SparepartsCatData? = null
) : BaseResponse() {
    data class SparepartsCatData(
        @SerializedName("list") val list: List<Category>? = null
    )
}