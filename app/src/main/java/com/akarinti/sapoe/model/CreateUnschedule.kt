package com.akarinti.sapoe.model


import com.google.gson.annotations.SerializedName

data class CreateUnschedule(
    @SerializedName("agent")
    val agent: Any? = null,
    @SerializedName("assignedBy")
    val assignedBy: AssignedBy = AssignedBy(),
    @SerializedName("assigned_date")
    val assignedDate: String = "",
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("day")
    val day: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("location")
    val location: Location = Location(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("updated_at")
    val updatedAt: Int = 0
) {
    data class AssignedBy(
        @SerializedName("created_at")
        val createdAt: Int = 0,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("updated_at")
        val updatedAt: Int = 0
    )

    data class Location(
        @SerializedName("ac_daily_visit")
        val acDailyVisit: Int = 0,
        @SerializedName("ac_monthly_frequency")
        val acMonthlyFrequency: Int = 0,
        @SerializedName("ac_monthly_visit")
        val acMonthlyVisit: Int = 0,
        @SerializedName("address")
        val address: String = "",
        @SerializedName("atm_daily_visit")
        val atmDailyVisit: Int = 0,
        @SerializedName("atm_monthly_frequency")
        val atmMonthlyFrequency: Int = 0,
        @SerializedName("atm_monthly_visit")
        val atmMonthlyVisit: Int = 0,
        @SerializedName("atm_weekly_visit")
        val atmWeeklyVisit: Int = 0,
        @SerializedName("created_at")
        val createdAt: Int = 0,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("is_active")
        val isActive: Boolean = false,
        @SerializedName("lat")
        val lat: Double = 0.0,
        @SerializedName("long")
        val long: Double = 0.0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("nb_daily_visit")
        val nbDailyVisit: Int = 0,
        @SerializedName("nb_monthly_frequency")
        val nbMonthlyFrequency: Int = 0,
        @SerializedName("nb_monthly_visit")
        val nbMonthlyVisit: Int = 0,
        @SerializedName("pic_contact_number")
        val picContactNumber: String = "",
        @SerializedName("pic_name")
        val picName: String = "",
        @SerializedName("room_type")
        val roomType: String = "",
        @SerializedName("updated_at")
        val updatedAt: Int = 0,
        @SerializedName("zip_code")
        val zipCode: Int = 0
    )
}