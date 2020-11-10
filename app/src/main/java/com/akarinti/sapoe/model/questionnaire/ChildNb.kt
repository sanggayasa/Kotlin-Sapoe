package com.akarinti.sapoe.model.questionnaire
import com.google.gson.annotations.SerializedName

data class ChildNb(
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    var isDone: Boolean? = false
)
