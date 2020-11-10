package com.akarinti.sapoe.view.main.visit.adapter

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.objects.Params
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_visit.view.*

class ScheduleCompleteAdapter(data: ArrayList<Schedule>) : BaseQuickAdapter<Schedule, BaseViewHolder>(R.layout.item_visit, data) {
    override fun convert(helper: BaseViewHolder?, item: Schedule?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.ivType.setImageResource(
                    when(i.type) {
                        Params.TYPE_AC -> R.drawable.label_ac
                        Params.TYPE_NEONBOX -> R.drawable.label_nb
                        Params.TYPE_CLEAN -> R.drawable.label_clean
                        else -> R.drawable.label_qc
                    })

                h.itemView.tvProcess.visibility = View.GONE
                h.itemView.ivDone.visibility = View.VISIBLE

                h.itemView.tvPlace.text = i.location?.name?:"-"
                h.itemView.tvAddress.text = i.location?.address?:"-"
            }
        }
    }
}