package com.akarinti.sapoe.objects

import androidx.annotation.IntDef

@IntDef(
    CodeIntent.UPLOAD_IMAGE,
    CodeIntent.APP_SETTINGS
)
annotation class CodeIntent {
    companion object {
        const val UPLOAD_IMAGE = 1
        const val APP_SETTINGS = UPLOAD_IMAGE + 1
        const val OPEN_QUESTION = APP_SETTINGS + 1
    }

}