package com.akarinti.sapoe.view.main.unscheduled.arsip

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.data.response.UnscheduleListResponse
import com.akarinti.sapoe.event.UpdateStatusUnscheduleEvent
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.extension.rounded
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.main.unscheduled.adapter.UnscheduleCompleteAdapterAuto
import com.akarinti.sapoe.view.main.unscheduled.adapter.UnscheduleCompleteAdapterManual
import com.akarinti.sapoe.view.main.unscheduled.answer_parent.UnscheduleAnswerParentActivity
import dagger.android.support.AndroidSupportInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.fragment_unscheduled_list.*
import javax.inject.Inject

class UnscheduledArsipFragment : BaseMvpFragment<UnscheduledArsipPresenter>(),
    UnscheduledArsipContract.View {

    @Inject
    override lateinit var presenter: UnscheduledArsipPresenter

    private var listA = ArrayList<Unschedule>()
    private var listM = ArrayList<Unschedule>()
    private lateinit var autoAdapter: UnscheduleCompleteAdapterAuto
    private lateinit var manualAdapter: UnscheduleCompleteAdapterManual
    private var pageA = 1
    private var pageM = 1

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun setup() {
        initView()
        getScheduleA(1)
        getScheduleM(1)
    }

    private fun initView() {
        autoAdapter = UnscheduleCompleteAdapterAuto(listA)
        rvUnscheduledListAuto.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = autoAdapter
            isNestedScrollingEnabled = false
        }
        autoAdapter.apply {
            setOnItemClickListener { _, _, position ->
                this@UnscheduledArsipFragment.activity?.let {
                    UnscheduleAnswerParentActivity.newInstance(it, autoAdapter.data[position])
                }
            }
            emptyView = LayoutInflater.from(context)
                .inflate(R.layout.empty_unschedule, rvUnscheduledListAuto, false)
        }
        cvLoadmoreA.setOnClickListener { onLoadMoreA() }

        manualAdapter = UnscheduleCompleteAdapterManual(listM)

        rvUnscheduledListManual.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = manualAdapter
            isNestedScrollingEnabled = false

        }
        manualAdapter.apply {
            setOnItemClickListener { _, _, position ->
                this@UnscheduledArsipFragment.activity?.let {
                    UnscheduleAnswerParentActivity.newInstance(it, manualAdapter.data[position])
                }
            }
            emptyView = LayoutInflater.from(context)
                .inflate(R.layout.empty_schedule, rvUnscheduledListManual, false)
        }
        cvLoadmoreM.setOnClickListener { onLoadMoreM() }
    }

    private fun getScheduleA(page: Int) {
        showLoading()
        presenter.getCompleteUnschedule(
            DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
            DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5),
            requireActivity().isNetworkAvailable(),
            page
        )
    }

    private fun getScheduleM(page: Int) {
        showLoading()
        presenter.getCompleteUnscheduleManual(
            DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
            DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5),
            requireActivity().isNetworkAvailable(),
            page
        )
    }

    private fun onLoadMoreA() {
        pageA++
        getScheduleA(pageA)
    }

    private fun onLoadMoreM() {
        pageM++
        getScheduleM(pageM)
    }

    override fun gotoCompleteUnschedulePager(data: UnscheduleListResponse) {
        if (pageA == 1)
            listA.clear()

        data.data?.list?.let {
            listA.addAll(it)
        }
        autoAdapter.notifyDataSetChanged()
        var loadMore = false
        data.data?.pagination?.let {
            pageA = it.current_page
            loadMore = pageA < it.totalPage
        }
        cvLoadmoreA.visibility = if (loadMore) View.VISIBLE else View.GONE
        dismissLoading()
    }

    override fun gotoCompleteUnscheduleManualPager(data: UnscheduleListResponse) {
        if (pageM == 1)
            listM.clear()

        data.data?.list?.let {
            listM.addAll(it)
        }
        manualAdapter.notifyDataSetChanged()
        var loadMore = false
        data.data?.pagination?.let {
            pageM = it.current_page
            loadMore = pageM < it.totalPage
        }
        cvLoadmoreM.visibility = if (loadMore) View.VISIBLE else View.GONE
        dismissLoading()
    }

    override fun getLayout(): Int = R.layout.fragment_unscheduled_list

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(UpdateStatusUnscheduleEvent::class.java).subscribe {
            pageA = 1
            pageM = 1
            getScheduleA(1)
            getScheduleM(1)
        })
    }
}