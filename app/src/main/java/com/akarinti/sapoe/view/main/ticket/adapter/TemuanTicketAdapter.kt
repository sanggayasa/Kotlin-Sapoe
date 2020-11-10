package com.akarinti.sapoe.view.main.ticket.adapter

import android.util.Log
import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.ticket.Ticket
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_ticket_add.view.*


class TemuanTicketAdapter(data: ArrayList<Ticket>) : BaseQuickAdapter<Ticket, BaseViewHolder>(
    R.layout.item_ticket_add, data) {

    override fun convert(helper: BaseViewHolder?, item: Ticket?) {
        helper?.let { h ->
            item?.let { i ->
                val point:Double = h.position.toDouble()+1
                h.itemView.tvTicket.text = "Tiket "+point.toInt()
                h.itemView.tvType.text = "Tipe Tiket "+i.categoryType
                h.itemView.tvJenis.text = "Jenis Tiket "+i.sla?.description
            }
        }
    }
}