package com.akarinti.sapoe.view.main.ticket.adapter

import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.ticket.Category
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_route.view.*


class SparepartsCatAdapter(list: List<Category>) : BaseQuickAdapter<Category, BaseViewHolder>(
    R.layout.item_route, list) {
    override fun convert(helper: BaseViewHolder?, item: Category?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvRouteName.text = i.name
            }
        }
    }
}