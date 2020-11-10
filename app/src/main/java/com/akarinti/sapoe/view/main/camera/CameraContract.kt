package com.akarinti.sapoe.view.main.camera

import com.akarinti.sapoe.base.ErrorView

interface CameraContract {
    interface View: ErrorView {
        fun gotoNextPage()
    }

    interface Presenter{
        fun loadFoto()
    }
}