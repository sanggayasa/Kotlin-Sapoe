package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.questionnaire.QuestionDetail
import com.google.gson.annotations.SerializedName

data class QuestionDetailListResponse(@SerializedName("data") val data: Data? = null
) : BaseResponse() {
    data class Data(
        @SerializedName("id") val id: String? = null,
        @SerializedName("type") val type: String? = null,
        @SerializedName("question_list") val questionList: List<QuestionDetail>? = null)
}