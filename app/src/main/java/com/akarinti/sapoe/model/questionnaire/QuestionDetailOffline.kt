package com.akarinti.sapoe.model.questionnaire

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionDetailOffline(
    @SerializedName("id") var id: String? = null,
    @SerializedName("question") val question: String? = null,
    @SerializedName("answer_type") val answerType: String? = null,
    @SerializedName("answer_list") val answerList: List<String>? = null,
    @SerializedName("answer_length") val answerLength: Int? = null,
    @SerializedName("is_required") val isRequired: Boolean? = null,
    @SerializedName("index") val index: Int? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("answer") val answerData: Answer? = null,
    var answerLocal: String? = null,
    var isDone: Boolean? = false
) : Parcelable