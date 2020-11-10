package com.akarinti.sapoe.model

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("limit") val limit: Int = 0,
    @SerializedName("total_page") val totalPage: Int = 0,
    @SerializedName("total_rows") val total_rows: Int = 0,
    @SerializedName("current_page") val current_page: Int = 0
)