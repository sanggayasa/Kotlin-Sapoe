package com.akarinti.sapoe.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AgentSchedule(
    @SerializedName("id") val id: String? = null,
    @SerializedName("effective_date") val effectiveDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null
) : Parcelable