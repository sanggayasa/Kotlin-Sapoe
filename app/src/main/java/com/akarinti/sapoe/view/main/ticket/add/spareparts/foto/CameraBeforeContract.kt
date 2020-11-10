package com.akarinti.sapoe.view.main.ticket.add.spareparts.foto


import com.akarinti.sapoe.base.ErrorView

interface CameraBeforeContract {
    interface View: ErrorView {
        fun gotoNextPage()
    }

    interface Presenter{
        fun loadFoto()
    }
}