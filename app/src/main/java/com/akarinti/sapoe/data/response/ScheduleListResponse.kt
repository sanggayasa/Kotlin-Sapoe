package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Pagination
import com.akarinti.sapoe.model.Schedule
import com.google.gson.annotations.SerializedName

data class ScheduleListResponse(@SerializedName("data") val data: ScheduleListData? = null
) : BaseResponse() {
    data class ScheduleListData(
        @SerializedName("list") val list: List<Schedule>? = null,
        @SerializedName("pagination") val pagination: Pagination = Pagination()
    )
}