package com.akarinti.sapoe.model
import com.google.gson.annotations.SerializedName


data class Version(
    @SerializedName("update_app") val updateApp: Boolean? = null,
    @SerializedName("force_update") val forceUpdate: Boolean? = null,
    @SerializedName("url") val url: String? = null
)