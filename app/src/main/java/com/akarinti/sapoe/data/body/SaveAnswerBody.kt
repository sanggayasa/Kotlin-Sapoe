package com.akarinti.sapoe.data.body

import com.google.gson.annotations.SerializedName

data class SaveAnswerBody(
    @SerializedName("order_id") val orderId: String = "",
    @SerializedName("order_type") val orderType: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("answer_list") val answerList: List<Answer> = listOf(),
    @SerializedName("duration") val duration: List<Duration> = listOf(),
    val suhuAnswer: String = "",
    val suhuPicture: String = ""
    ) {
    data class Answer(
        @SerializedName("item_id") val itemId: String = "",
        @SerializedName("question_id") val questionId: String = "",
        @SerializedName("answer") val answer: String = ""
    )

    data class Duration(
        @SerializedName("item_id") val itemId: String = "",
        @SerializedName("start") val start: Long? = null,
        @SerializedName("end") val end: Long? = null
    )
}