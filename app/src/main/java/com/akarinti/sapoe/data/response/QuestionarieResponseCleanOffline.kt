package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.google.gson.annotations.SerializedName

class QuestionarieResponseCleanOffline (@SerializedName("data") val data: ParentCleanOffline? = null): BaseResponse()