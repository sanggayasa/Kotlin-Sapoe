package com.akarinti.sapoe.model.answer

data class AnswerScheduled(
    var orderID: String,
    var orderType: String,
    var formId: String?,
    var itemID: String? = null,
    var questionID: String,
    var answer: String
)