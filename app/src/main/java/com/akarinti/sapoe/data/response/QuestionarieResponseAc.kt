package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentAc
import com.google.gson.annotations.SerializedName

class QuestionarieResponseAc (@SerializedName("data") val data: ParentAc? = null): BaseResponse()