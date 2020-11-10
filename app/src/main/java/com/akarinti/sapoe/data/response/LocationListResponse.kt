package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Pagination
import com.akarinti.sapoe.model.Route
import com.google.gson.annotations.SerializedName

data class LocationListResponse(
    @SerializedName("data") val data: LocationListData? = null
) : BaseResponse() {
    data class LocationListData(
        @SerializedName("list") val list: List<Route>? = null,
        @SerializedName("pagination") val pagination: Pagination? = null
    )
}