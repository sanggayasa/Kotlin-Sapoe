package com.akarinti.sapoe.view.main.ticket.adapter

import androidx.core.content.ContextCompat
import com.akarinti.sapoe.R
import com.akarinti.sapoe.extension.dateFormat
import com.akarinti.sapoe.model.ticket.Ticket
import com.akarinti.sapoe.objects.Params
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_ticket.view.*


class TicketListAdapter(data: ArrayList<Ticket>) : BaseQuickAdapter<Ticket, BaseViewHolder>(R.layout.item_ticket, data) {

    override fun convert(helper: BaseViewHolder?, item: Ticket?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvProcess.apply {
                    text = mContext.getString(if(i.status == Params.STATUS_OPEN) R.string.open else R.string.in_progress)
                    setTextColor(ContextCompat.getColor(mContext, if(i.status == Params.STATUS_OPEN) R.color.blue_cobalt else R.color.orange_tangerine))
                }

                h.itemView.tvPlaceTicket.text = i.location?.name?:"-"
                h.itemView.tvAddressTicket.text = i.location?.address?:"-"
                h.itemView.tvJenis.text =  mContext.getString(R.string.jenis_tiket_fmt, i.ticketType)
                h.itemView.tvId.text = mContext.getString(R.string.tiket_id_fmt, i.id)
                h.itemView.tvTicketDate.text = mContext.getString(R.string.tanggal_fmt, ((i.createdAt?:0L) * 1000L).dateFormat("dd MMM yyyy, HH:mm"))
                h.itemView.tvReported.text = mContext.getString(R.string.reported_fmt, i.createdBy?.name)
                h.itemView.tvSla.text = mContext.getString(R.string.sla_fmt, i.sla?.description)
            }
        }
    }
}