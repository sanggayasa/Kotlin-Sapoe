package com.akarinti.sapoe.view.splash

import com.akarinti.sapoe.base.ErrorView

interface SplashContract {
    interface View: ErrorView {
        fun onNextScreen(isLoggedIn: Boolean)
    }

    interface Presenter {
        fun getToken()
    }
}