package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Token
import com.google.gson.annotations.SerializedName

class TokenResponse(@SerializedName("data") val data: Token? = null) : BaseResponse()