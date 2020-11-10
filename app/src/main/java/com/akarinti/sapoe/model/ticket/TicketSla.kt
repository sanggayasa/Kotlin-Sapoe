package com.akarinti.sapoe.model.ticket


import com.google.gson.annotations.SerializedName

data class TicketSla(
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("day")
    val day: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("penalty_four_week")
    val penaltyFourWeek: Int = 0,
    @SerializedName("penalty_more_week")
    val penaltyMoreWeek: Int = 0,
    @SerializedName("penalty_one_week")
    val penaltyOneWeek: Int = 0,
    @SerializedName("penalty_three_week")
    val penaltyThreeWeek: Int = 0,
    @SerializedName("penalty_two_week")
    val penaltyTwoWeek: Int = 0,
    @SerializedName("updated_at")
    val updatedAt: Int = 0
)