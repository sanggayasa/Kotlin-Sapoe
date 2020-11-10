package com.akarinti.sapoe.view.main.ticket.add.foto

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.answer.ImageResult


interface FotoAfterContract {
    interface View: ErrorView {
        fun gotoNextPage()
    }

    interface Presenter{
        fun getRepository(): List<ImageResult>
    }
}