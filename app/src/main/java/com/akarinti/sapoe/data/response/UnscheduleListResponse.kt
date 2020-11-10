package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Pagination
import com.akarinti.sapoe.model.Unschedule
import com.google.gson.annotations.SerializedName


data class UnscheduleListResponse(@SerializedName("data") val data: UnscheduleListData? = null
) : BaseResponse() {
    data class UnscheduleListData(
        @SerializedName("list") val list: List<Unschedule>? = null,
        @SerializedName("pagination") val pagination: Pagination = Pagination()
    )
}