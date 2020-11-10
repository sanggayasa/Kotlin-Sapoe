package com.akarinti.sapoe.view.main.visit.visit_parent

import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.Question
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_visit_parent.view.*

class VisitParentAdapter(data: ArrayList<Question>) : BaseQuickAdapter<Question, BaseViewHolder>(
    R.layout.item_visit_parent, data) {

    override fun convert(helper: BaseViewHolder?, item: Question?) {
        helper?.let { h ->
            item?.let { i ->

                h.itemView.tvPlaceParent.text= i.question
                h.itemView.tvWsid.text = "WSID : "+i.id
            }
        }
    }
}