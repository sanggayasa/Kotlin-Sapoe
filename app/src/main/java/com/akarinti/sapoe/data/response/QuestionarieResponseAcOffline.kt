package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.google.gson.annotations.SerializedName

class QuestionarieResponseAcOffline (@SerializedName("data") val data: ParentAcOffline? = null): BaseResponse()