package com.akarinti.sapoe.view.main.home

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.NewsListResponse
import com.akarinti.sapoe.model.MenuCount

interface HomeContract {
    interface View : ErrorView {
        fun onMenuCount(data: MenuCount?)
        fun onNews(dataList: NewsListResponse)
    }
    interface Presenter {
        fun getMenuCount()
        fun getNews(page: Int = 1, limit: Int = 3)
    }
}