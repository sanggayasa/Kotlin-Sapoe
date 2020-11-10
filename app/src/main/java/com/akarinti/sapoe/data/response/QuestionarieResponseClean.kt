package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentClean
import com.google.gson.annotations.SerializedName

class QuestionarieResponseClean (@SerializedName("data") val data: ParentClean? = null): BaseResponse()