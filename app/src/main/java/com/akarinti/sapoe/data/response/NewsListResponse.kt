package com.akarinti.sapoe.data.response
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Pagination
import com.akarinti.sapoe.model.news.News
import com.google.gson.annotations.SerializedName


data class NewsListResponse(@SerializedName("data") val data: Data? = null) : BaseResponse() {
    data class Data(
        @SerializedName("list") val list: List<News>? = null,
        @SerializedName("pagination") val pagination: Pagination? = null
    )
}