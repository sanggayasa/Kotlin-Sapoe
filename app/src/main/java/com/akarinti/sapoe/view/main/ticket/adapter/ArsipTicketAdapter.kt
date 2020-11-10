package com.akarinti.sapoe.view.main.ticket.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.extension.dateFormat
import com.akarinti.sapoe.model.ticket.Ticket
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_ticket.view.*


class ArsipTicketAdapter(data: ArrayList<Ticket>) : BaseQuickAdapter<Ticket, BaseViewHolder>(
    R.layout.item_ticket, data) {

    override fun convert(helper: BaseViewHolder?, item: Ticket?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvProcess.visibility= View.GONE
                h.itemView.ivDone.visibility=View.VISIBLE
                h.itemView.tvPlaceTicket.text = i.location?.name?:"-"
                h.itemView.tvAddressTicket.text = i.location?.address?:"-"
                //h.itemView.separator.visibility = View.GONE
                h.itemView.tvJenis.text =  mContext.getString(R.string.jenis_tiket_fmt, i.ticketType)
                h.itemView.tvId.text = mContext.getString(R.string.tiket_id_fmt, i.id)
                h.itemView.tvTicketDate.text = mContext.getString(R.string.tanggal_fmt, ((i.createdAt?:0L) * 1000L).dateFormat("dd MMM yyyy, HH:mm"))
                h.itemView.tvReported.text = mContext.getString(R.string.reported_fmt, i.createdBy?.name)
                h.itemView.tvSla.text = mContext.getString(R.string.sla_fmt, i.sla?.description)
            }
        }
    }
}