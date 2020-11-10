package com.akarinti.sapoe.view.main.visit.visit_parent.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.questionnaire.ChildCleanOffline
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_child.view.*

class ChildCleanOfflineAdapter(data: List<ChildCleanOffline>, val placeName: String) : BaseQuickAdapter<ChildCleanOffline, BaseViewHolder>(R.layout.item_child, data) {

    override fun convert(helper: BaseViewHolder?, item: ChildCleanOffline?) {
        helper?.let { h ->
            item?.let { cl ->
                h.itemView.tvChildName.text = String.format(mContext.getString(R.string.name_cl_fmt), placeName, (h.adapterPosition+1))
                h.itemView.tvInfo.text = String.format(mContext.getString(R.string.wsid_fmt), cl.wsid)
//                h.itemView.tvStatus.visibility = if (cl.isDone == true) View.INVISIBLE else View.VISIBLE
                h.itemView.ivFinish.visibility = if (cl.isDone == true) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}