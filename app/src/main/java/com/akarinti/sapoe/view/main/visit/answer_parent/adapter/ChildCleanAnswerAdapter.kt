package com.akarinti.sapoe.view.main.visit.answer_parent.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.questionnaire.ChildCleanOffline
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_child.view.*

class ChildCleanAnswerAdapter(data: List<ChildCleanOffline>, val placeName: String) : BaseQuickAdapter<ChildCleanOffline, BaseViewHolder>(R.layout.item_child, data) {

    override fun convert(helper: BaseViewHolder?, item: ChildCleanOffline?) {
        helper?.let { h ->
            item?.let { cl ->
                h.itemView.tvChildName.text = String.format(mContext.getString(R.string.name_cl_fmt), placeName, (h.adapterPosition+1))
                h.itemView.tvInfo.text = String.format(mContext.getString(R.string.wsid_fmt), cl.wsid)
                h.itemView.tvStatus.visibility = View.INVISIBLE
                h.itemView.ivFinish.visibility = View.VISIBLE
            }
        }
    }
}