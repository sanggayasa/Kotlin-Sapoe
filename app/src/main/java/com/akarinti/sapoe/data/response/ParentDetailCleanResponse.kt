package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentDetailClean
import com.google.gson.annotations.SerializedName

class ParentDetailCleanResponse (@SerializedName("data") val data: ParentDetailClean? = null): BaseResponse()