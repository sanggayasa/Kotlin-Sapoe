package com.akarinti.sapoe.view.main.visit.visit_parent.adapter

import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.questionnaire.QuestionDetail
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.spinner_default.view.*


class ComboBoxAdapter(data: List<QuestionDetail>) : BaseQuickAdapter<QuestionDetail, BaseViewHolder>(R.layout.spinner_default, data) {

    override fun convert(helper: BaseViewHolder?, item: QuestionDetail?) {
        helper?.let { h ->
            item?.let { question ->
                h.itemView.tvQuestion .text = question.question
            }
        }
    }
}