package com.akarinti.sapoe.view.main.visit.arsip

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.data.response.ScheduleListResponse
import com.akarinti.sapoe.event.UpdateStatusScheduledEvent
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.main.visit.adapter.ScheduleCompleteAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.AnswerParentActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_visit_list.*
import javax.inject.Inject

class VisitArsipFragment : BaseMvpFragment<VIsitArsipPresenter>(), VisitArsipContract.View {

    @Inject
    override lateinit var presenter: VIsitArsipPresenter

    private lateinit var visitListadapter: ScheduleCompleteAdapter
    private var list = ArrayList<Schedule>()
    private var page = 1

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }
    override fun setup() {
        initView()
        getSchedule(1)
    }
    private fun initView(){
        visitListadapter = ScheduleCompleteAdapter(list)
        rvVisitList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = visitListadapter
            isNestedScrollingEnabled = false
        }
        visitListadapter.apply {
            setOnItemClickListener { _, _, position ->
                this@VisitArsipFragment.activity?.let {
                    AnswerParentActivity.newInstance(it, visitListadapter.data[position])
                }
            }
            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_schedule, rvVisitList, false)
        }
        cvLoadmore.setOnClickListener { onLoadMore() }
    }

    override fun getLayout(): Int = R.layout.fragment_visit_list

    private fun onLoadMore() {
        page++
        getSchedule(page)
    }

    private fun getSchedule(page: Int) {
        showLoading()
        presenter.getCompletedSchedule(page)
    }

    override fun onCompletedSchedulePager(data: ScheduleListResponse) {
        if (page == 1)
            list.clear()

        data.data?.list?.let {
            list.addAll(it)
        }
        visitListadapter.notifyDataSetChanged()
        var loadMore = false
        data.data?.pagination?.let {
            page = it.current_page
            loadMore = page < it.totalPage
        }
        cvLoadmore.visibility = if (loadMore) View.VISIBLE else View.GONE
        dismissLoading()
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(UpdateStatusScheduledEvent::class.java).subscribe {
            page = 1
            getSchedule(1)
        })
    }
}