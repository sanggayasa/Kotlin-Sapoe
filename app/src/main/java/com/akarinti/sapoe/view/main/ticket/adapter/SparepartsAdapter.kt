package com.akarinti.sapoe.view.main.ticket.adapter

import android.annotation.SuppressLint
import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.ticket.SparepartList
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_spareparts.view.*


class SparepartsAdapter(list: List<SparepartList>, val isShowDelete: Boolean = false) : BaseQuickAdapter<SparepartList, BaseViewHolder>(
    R.layout.item_spareparts, list) {
    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder?, item: SparepartList?) {
        helper?.let { h ->
            item?.let { i ->
                val point:Double = h.position.toDouble()+1
                h.itemView.tvParts.text = "Spareparts "+point.toInt()
                h.itemView.tvJenisParts.text = "Jenis Spareparts: "+i.sparepart?.name
                if (isShowDelete) {
                    h.addOnClickListener(R.id.ivDelete)
                    h.itemView.ivDelete.visibility = View.VISIBLE
                }
            }
        }
    }
}