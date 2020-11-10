package com.akarinti.sapoe.model


import com.google.gson.annotations.SerializedName

open class LoginModel(
    @SerializedName("contact_number")
    val contactNumber: Any? = null,
    @SerializedName("created_at")
    val createdAt: Any? = null,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("role_type")
    val roleType: Any? = null,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("updated_at")
    val updatedAt: Any? = null,
    @SerializedName("username")
    val username: String = ""
)
