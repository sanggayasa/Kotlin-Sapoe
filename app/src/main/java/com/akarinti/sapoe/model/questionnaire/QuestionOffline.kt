package com.akarinti.sapoe.model.questionnaire
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionOffline(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("checklist") val checklist: Checklist? = null,
    @SerializedName("index") val index: Int? = null,
    @SerializedName("question_list") var questionList: List<QuestionDetailOffline>? = null,
    var isDone: Boolean? = false
) : Parcelable