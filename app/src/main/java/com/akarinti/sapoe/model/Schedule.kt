package com.akarinti.sapoe.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
    @SerializedName("id") val id: String? = null,
    @SerializedName("agent") val agent: Agent? = null,
    @SerializedName("assigned_date") val assignedDate: String? = null,
    @SerializedName("day") val day: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("agent_schedule") val agentSchedule: AgentSchedule? = null,
    @SerializedName("distance") val distance: Double? = null,
    @SerializedName("location") val location: Location? = null,
    var isEnable: Boolean = true
) : Parcelable {
    @Parcelize
    data class Agent(
        @SerializedName("id") val id: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("type") val type: String? = null
    ) : Parcelable
}