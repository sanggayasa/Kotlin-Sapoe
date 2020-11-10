package com.akarinti.sapoe.model.questionnaire
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildNbOffline(
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("index") val index: Int? = null,
    @SerializedName("form_list") var formList: List<QuestionOffline>? = null,
    var isDone: Boolean? = false
) : Parcelable
