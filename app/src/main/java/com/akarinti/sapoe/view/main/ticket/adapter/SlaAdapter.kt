package com.akarinti.sapoe.view.main.ticket.adapter

import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.ticket.TicketSla
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_route.view.*


class SlaAdapter(list: List<TicketSla>) : BaseQuickAdapter<TicketSla, BaseViewHolder>(R.layout.item_route, list) {
    override fun convert(helper: BaseViewHolder?, item: TicketSla?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvRouteName.text = i.description
            }
        }
    }
}