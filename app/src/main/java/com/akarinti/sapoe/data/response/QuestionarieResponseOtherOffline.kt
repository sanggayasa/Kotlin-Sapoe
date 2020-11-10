package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentOtherOffline
import com.google.gson.annotations.SerializedName

class QuestionarieResponseOtherOffline (@SerializedName("data") val data: ParentOtherOffline? = null): BaseResponse()