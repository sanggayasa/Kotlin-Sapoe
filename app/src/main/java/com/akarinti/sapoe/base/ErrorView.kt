package com.akarinti.sapoe.base

import com.akarinti.sapoe.model.Version

interface ErrorView {
    fun errorScreen(message: String? = "", code: Int? = -1)
    fun errorScreen(message: String? = "")
    fun forceLogout()
    fun errorConnection()
    fun onVersionCheck(data: Version?)
}