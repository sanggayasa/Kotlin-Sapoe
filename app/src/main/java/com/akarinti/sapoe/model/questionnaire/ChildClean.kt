package com.akarinti.sapoe.model.questionnaire
import com.google.gson.annotations.SerializedName

data class ChildClean(
    @SerializedName("id") val id: String? = null,
    @SerializedName("wsid") val wsid: String? = null,
    @SerializedName("booth_type") val boothType: String? = null,
    @SerializedName("machine_type") val machineType: String? = null,
    @SerializedName("contract_status") val contractStatus: String? = null,
    @SerializedName("is_trash_ready") val isTrashReady: Boolean? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    var isDone: Boolean? = false
)