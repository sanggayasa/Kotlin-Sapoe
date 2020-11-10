package com.akarinti.sapoe.view.main.other.adapter

import com.akarinti.sapoe.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(data: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_menu, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvMenu.text = i
            }
        }
    }
}