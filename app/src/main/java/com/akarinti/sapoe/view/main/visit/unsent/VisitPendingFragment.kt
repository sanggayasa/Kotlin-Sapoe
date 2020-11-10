package com.akarinti.sapoe.view.main.visit.unsent

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.event.UpdateSEvent
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.main.visit.adapter.ScheduleUnsentAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_visit_list.*
import javax.inject.Inject

class VisitPendingFragment : BaseMvpFragment<VisitPendingPresenter>(), VisitPendingContract.View {

    @Inject
    override lateinit var presenter: VisitPendingPresenter

    private lateinit var visitListadapter: ScheduleUnsentAdapter

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }
    override fun setup() {
        initView()
    }
    private fun initView(){
    visitListadapter = ScheduleUnsentAdapter(presenter.unsentList())
        rvVisitList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = visitListadapter
            isNestedScrollingEnabled = false
        }
        visitListadapter.emptyView = LayoutInflater.from(context).inflate(R.layout.empty_schedule, rvVisitList, false)
    }

    override fun getLayout(): Int = R.layout.fragment_visit_list

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(UpdateSEvent::class.java).subscribe {
            visitListadapter.setNewData(it.dataList)
            visitListadapter.notifyDataSetChanged()
        })
    }
}