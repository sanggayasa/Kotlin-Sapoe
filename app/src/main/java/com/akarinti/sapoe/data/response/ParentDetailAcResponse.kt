package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.ParentDetailAc
import com.google.gson.annotations.SerializedName

class ParentDetailAcResponse (@SerializedName("data") val data: ParentDetailAc? = null): BaseResponse()