package com.akarinti.sapoe.model.ticket


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null
)