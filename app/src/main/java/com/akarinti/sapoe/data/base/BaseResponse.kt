package com.akarinti.sapoe.data.base

import com.akarinti.sapoe.model.Metadata
import com.google.gson.annotations.SerializedName

open class BaseResponse(@SerializedName("meta") val meta: Metadata? = null)