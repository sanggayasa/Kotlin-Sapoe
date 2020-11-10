package com.akarinti.sapoe.data.response
import com.akarinti.sapoe.data.base.BaseResponse
import com.google.gson.annotations.SerializedName


data class WhitelistAppResponse(
    @SerializedName("data") val data: DataApp? = null
): BaseResponse() {
    data class DataApp(
        @SerializedName("list") val list: List<App>? = null
    ) {
        data class App (
            @SerializedName("app_name") val appName: String? = null,
            @SerializedName("id") val id: String? = null
        )
    }

}