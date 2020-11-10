package com.akarinti.sapoe.model


import com.google.gson.annotations.SerializedName

data class Questionaire(
    @SerializedName("checklist")
    val checklist: Checklist = Checklist(),
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("question_list")
    val questionList: List<Question> = listOf(),
    @SerializedName("question_number")
    val questionNumber: Int = 0,
    @SerializedName("updated_at")
    val updatedAt: Int = 0
) {
    data class Checklist(
        @SerializedName("created_at")
        val createdAt: Any? = null,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("parent")
        val parent: Parent = Parent(),
        @SerializedName("type")
        val type: String = "",
        @SerializedName("updated_at")
        val updatedAt: Any? = null
    ) {
        data class Parent(
            @SerializedName("created_at")
            val createdAt: Any? = null,
            @SerializedName("id")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("updated_at")
            val updatedAt: Any? = null
        )
    }


}