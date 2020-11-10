package com.akarinti.sapoe.data.response
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Version
import com.google.gson.annotations.SerializedName


data class VersionResponse(@SerializedName("data") val data: Version? = null): BaseResponse()