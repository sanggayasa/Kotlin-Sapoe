package com.akarinti.sapoe.view.main.ticket.add.spareparts

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.body.SparepartsBody
import com.akarinti.sapoe.model.ticket.Category
import com.akarinti.sapoe.model.ticket.Spareparts

interface SparepartsContract {
    interface View: ErrorView {
        fun onSparepartsList(list: List<Spareparts>?)
        fun onSparepartsCat(list: List<Category>?)
        fun onCreateSparepart()
        fun onCreateNext()
    }

    interface Presenter{
        fun loadParts()
        fun getSparepartsList(categoryId: String = "", q:String, cityID: String)
        fun getSparepartsCat()
        fun createSparepart(ticketId:String,input: SparepartsBody)
        fun createSparepartNext(ticketId:String,input: SparepartsBody)
    }
}