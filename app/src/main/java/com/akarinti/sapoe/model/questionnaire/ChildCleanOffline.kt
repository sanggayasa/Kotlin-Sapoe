package com.akarinti.sapoe.model.questionnaire
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildCleanOffline(
    @SerializedName("id") val id: String? = null,
    @SerializedName("wsid") val wsid: String? = null,
    @SerializedName("booth_type") val boothType: String? = null,
    @SerializedName("machine_type") val machineType: String? = null,
    @SerializedName("contract_status") val contractStatus: String? = null,
    @SerializedName("is_trash_ready") val isTrashReady: Boolean? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("index") val index: Int? = null,
    @SerializedName("form_list") var formList: List<QuestionOffline>? = null,
    var isDone: Boolean? = false
) : Parcelable