package com.akarinti.sapoe.view.main.unscheduled.unsent

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.event.UpdateUAEvent
import com.akarinti.sapoe.event.UpdateUMEvent
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.main.unscheduled.adapter.UnscheduledUnsentAutoAdapter
import com.akarinti.sapoe.view.main.unscheduled.adapter.UnscheduledUnsentManualAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_unscheduled_list.*
import javax.inject.Inject

class UnscheduledPendingFragment : BaseMvpFragment<UnscheduledPendingPresenter>(), UnscheduledPendingContract.View {

    @Inject
    override lateinit var presenter: UnscheduledPendingPresenter

    private lateinit var manualAdapter: UnscheduledUnsentManualAdapter
    private lateinit var autoAdapter: UnscheduledUnsentAutoAdapter

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
        manualAdapter = UnscheduledUnsentManualAdapter(presenter.manualList())
        autoAdapter = UnscheduledUnsentAutoAdapter(presenter.autoList())
        rvUnscheduledListAuto.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = autoAdapter
            isNestedScrollingEnabled = false
        }
        rvUnscheduledListManual.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = manualAdapter
            isNestedScrollingEnabled = false
        }
        autoAdapter.apply {

            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_unschedule, rvUnscheduledListAuto, false)
        }
        manualAdapter.apply {

            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_unschedule, rvUnscheduledListManual, false)
        }

    }

    override fun getLayout(): Int = R.layout.fragment_unscheduled_list

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(UpdateUMEvent::class.java).subscribe {
            manualAdapter.setNewData(it.dataList)
            manualAdapter.notifyDataSetChanged()
        })
        addUiSubscription(RxBus.listen(UpdateUAEvent::class.java).subscribe {
            autoAdapter.setNewData(it.dataList)
            autoAdapter.notifyDataSetChanged()
        })
    }
}