package com.akarinti.sapoe.view.main.visit.answer_parent.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.questionnaire.ChildAcOffline
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_child.view.*

class ChildAcAnswerAdapter(data: List<ChildAcOffline>, val placeName: String) : BaseQuickAdapter<ChildAcOffline, BaseViewHolder>(R.layout.item_child, data) {

    override fun convert(helper: BaseViewHolder?, item: ChildAcOffline?) {
        helper?.let { h ->
            item?.let { ac ->
                h.itemView.tvChildName.text = String.format(mContext.getString(R.string.name_ac_fmt), placeName, (h.adapterPosition+1))
                h.itemView.tvInfo.text = String.format(mContext.getString(R.string.serial_no_fmt), ac.indoorSerialNumber)
                h.itemView.tvStatus.visibility = View.INVISIBLE
                h.itemView.ivFinish.visibility = View.VISIBLE
            }
        }
    }
}