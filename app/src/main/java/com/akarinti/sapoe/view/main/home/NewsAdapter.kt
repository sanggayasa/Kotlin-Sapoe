package com.akarinti.sapoe.view.main.home

import com.akarinti.sapoe.R
import com.akarinti.sapoe.model.news.News
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(data: ArrayList<News>) : BaseQuickAdapter<News, BaseViewHolder>(R.layout.item_news, data) {

    override fun convert(helper: BaseViewHolder?, item: News?) {
        helper?.let { h ->
            item?.let { i ->
                h.itemView.tvNews.text = i.title
            }
        }
    }
}