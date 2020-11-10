package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.google.gson.annotations.SerializedName

class QuestionarieResponseNbOffline (@SerializedName("data") val data: ParentNbOffline? = null): BaseResponse()