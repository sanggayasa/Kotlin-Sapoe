package com.akarinti.sapoe.model

import com.google.gson.annotations.SerializedName


data class Question(
    @SerializedName("answer_length")
    val answerLength: String = "",
    @SerializedName("answer_list")
    val answerList: String = "",
    @SerializedName("answer_type")
    val answerType: String = "",
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("index")
    val index: Int = 0,
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("is_required")
    val isRequired: Boolean = false,
    @SerializedName("question")
    val question: String = "",
    @SerializedName("updated_at")
    val updatedAt: Int = 0
)