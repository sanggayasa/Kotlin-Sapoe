package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentNb
import com.google.gson.annotations.SerializedName

class QuestionarieResponseNb (@SerializedName("data") val data: ParentNb? = null): BaseResponse()