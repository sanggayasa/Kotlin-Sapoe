package com.akarinti.sapoe.model.questionnaire
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("checklist") val checklist: Checklist? = null,
    var isDone: Boolean? = false
) : Parcelable