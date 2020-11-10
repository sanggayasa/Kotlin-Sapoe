package com.akarinti.sapoe.view.main.visit.visit_parent.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.questionnaire.ChildNbOffline
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_child.view.*

class ChildNbOfflineAdapter(data: List<ChildNbOffline>, val placeName: String) : BaseQuickAdapter<ChildNbOffline, BaseViewHolder>(R.layout.item_child, data) {

    override fun convert(helper: BaseViewHolder?, item: ChildNbOffline?) {
        helper?.let { h ->
            item?.let { nb ->
                h.itemView.tvChildName.text = String.format(mContext.getString(R.string.name_nb_fmt), placeName, (h.adapterPosition+1))
                h.itemView.tvInfo.text = String.format(mContext.getString(R.string.tipe_nb_fmt), nb.type)
//                h.itemView.tvStatus.visibility = if (nb.isDone == true) View.INVISIBLE else View.VISIBLE
                h.itemView.ivFinish.visibility = if (nb.isDone == true) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}