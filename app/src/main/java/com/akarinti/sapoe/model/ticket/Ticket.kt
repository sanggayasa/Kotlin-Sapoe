package com.akarinti.sapoe.model.ticket


import android.os.Parcelable
import com.akarinti.sapoe.model.news.Office
import com.akarinti.sapoe.model.news.Vendor
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ticket(
    @SerializedName("id") val id: String? = null,
    @SerializedName("category_type") val categoryType: String? = null,
    @SerializedName("category_value") val categoryValue: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("image_before") val imageBefore: String? = null,
    @SerializedName("image_after") val imageAfter: String? = null,
    @SerializedName("ticket_type") val ticketType: String? = null,
    @SerializedName("order_type") val orderType: String? = null,
    @SerializedName("close_date") val closeDate: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
//    @SerializedName("updated_at") val updatedAt: Int? = null,
    @SerializedName("vendor") val vendor: Vendor? = null,
    @SerializedName("office") val office: Office? = null,
    @SerializedName("sla") val sla: Sla? = null,
    @SerializedName("agent") val agent: Agent? = null,
    @SerializedName("createdBy") val createdBy: Agent? = null,
    @SerializedName("location") val location: Location? = null,
    @SerializedName("closedBy") val closedBy: Agent? = null,
//    @SerializedName("unscheduledOrder") val unscheduledOrder: Any? = null,
//    @SerializedName("scheduledOrder") val scheduledOrder: Any? = null,
    @SerializedName("ac") val ac: Ac? = null,
    @SerializedName("atm") val atm: Atm?=null,
    @SerializedName("neonbox") val neonbox: Neonbox?=null,
    @SerializedName("sparepart_list") val sparepartList: List<SparepartList>? = null,
    @SerializedName("ongoing_sla") val ongoingSla: Int? = null
):Parcelable