package com.akarinti.sapoe.view.main.other

import com.akarinti.sapoe.base.ErrorView

interface OtherContract {
    interface View : ErrorView
    {
        fun onLogout()
    }
    interface Presenter
    {
        fun logoutUser()
    }
}