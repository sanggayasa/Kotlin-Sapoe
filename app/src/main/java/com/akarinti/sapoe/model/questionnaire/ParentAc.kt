package com.akarinti.sapoe.model.questionnaire
import com.akarinti.sapoe.model.Location
import com.google.gson.annotations.SerializedName

data class ParentAc(
    @SerializedName("id") val id: String? = null,
    @SerializedName("assigned_date") val assignedDate: String? = null,
    @SerializedName("day") val day: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("location") val location: Location? = null,
    @SerializedName("item_list") val itemList: List<ChildAc>? = null,
    @SerializedName("form_list") val question_list: List<Question>? = null
)