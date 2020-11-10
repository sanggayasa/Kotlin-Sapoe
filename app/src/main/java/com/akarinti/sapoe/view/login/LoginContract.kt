package com.akarinti.sapoe.view.login

import com.akarinti.sapoe.base.ErrorView

interface LoginContract {
    interface View: ErrorView {
        fun gotoNextPage()
    }

    interface Presenter{
        fun loginUser(username: String, password: String, deviceImei:String)
    }
}