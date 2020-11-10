package com.akarinti.sapoe.model.questionnaire
import android.os.Parcelable
import com.akarinti.sapoe.model.Location
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParentAcOffline(
    @SerializedName("id") val id: String? = null,
    @SerializedName("assigned_date") val assignedDate: String? = null,
    @SerializedName("day") val day: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("location") val location: Location? = null,
    @SerializedName("item_list") var itemList: List<ChildAcOffline>? = null,
    @SerializedName("form_list") var formList: List<QuestionOffline>? = null,
    @SerializedName("scheduleType") val scheduleType: String? = null,
    var start: Long? = null,
    var end: Long? = null,
    var agentName: String? = null,
    var agentID: String? = null
) : Parcelable