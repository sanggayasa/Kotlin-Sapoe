package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class SparepartsBody(@SerializedName("sparepart_list") val sparepartList: List<Sparepart>? = null) {
    data class Sparepart(
        @SerializedName("image_after") val imageAfter: String? = null,
        @SerializedName("image_before") val imageBefore: String? = null,
        @SerializedName("sparepart_id") val sparepartId: String? = null,
        @SerializedName("qty") val qty: Float? = null)
}