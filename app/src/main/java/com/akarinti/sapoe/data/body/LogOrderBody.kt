package com.akarinti.sapoe.data.body

import com.google.gson.annotations.SerializedName

data class LogOrderBody(
    @SerializedName("order_id") val orderId: String? = null,
    @SerializedName("order_type") val orderType: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("answer_list") val answerList: List<Answer>? = null,
    @SerializedName("temperature") val temperature: String? = null
    //@SerializedName("order_date") val orderDate: Long? = null
) {
    data class Answer(
        @SerializedName("item_id") val itemId: String = "",
        @SerializedName("question_id") val questionId: String = "",
        @SerializedName("answer") val answer: String = ""
    )
}