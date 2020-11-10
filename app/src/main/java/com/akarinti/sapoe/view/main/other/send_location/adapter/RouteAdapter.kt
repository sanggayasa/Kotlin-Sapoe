package com.akarinti.sapoe.view.main.other.send_location.adapter

import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_route.view.*

class RouteAdapter(list: List<Route>) : BaseQuickAdapter<Route, BaseViewHolder>(R.layout.item_route, list) {
    override fun convert(helper: BaseViewHolder?, item: Route?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvRouteName.text = i.name
            }
        }
    }
}