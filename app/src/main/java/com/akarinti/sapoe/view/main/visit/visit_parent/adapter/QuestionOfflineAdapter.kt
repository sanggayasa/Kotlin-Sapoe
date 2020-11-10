package com.akarinti.sapoe.view.main.visit.visit_parent.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.questionnaire.QuestionOffline
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionOfflineAdapter(data: List<QuestionOffline>) : BaseQuickAdapter<QuestionOffline, BaseViewHolder>(R.layout.item_question, data) {

    override fun convert(helper: BaseViewHolder?, item: QuestionOffline?) {
        helper?.let { h ->
            item?.let { question ->
                h.itemView.tvQuestionTitle.text = question.name
                h.itemView.ivCheck.setImageResource(if (question.isDone == true) R.drawable.ic_complete else R.drawable.ic_isi_form)
            }
        }
    }
}