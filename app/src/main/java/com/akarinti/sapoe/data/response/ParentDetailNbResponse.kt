package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentDetailNb
import com.google.gson.annotations.SerializedName

class ParentDetailNbResponse (@SerializedName("data") val data: ParentDetailNb? = null): BaseResponse()