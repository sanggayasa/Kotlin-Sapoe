package com.akarinti.sapoe.view.main.unscheduled.onprogress

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
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.unscheduled.adapter.UnscheduleOpenAdapterAuto
import com.akarinti.sapoe.view.main.unscheduled.adapter.UnscheduleOpenAdaptermanual
import com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.UnscheduleParentActivity
import dagger.android.support.AndroidSupportInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.fragment_unscheduled_list.*
import javax.inject.Inject

class UnscheduledListFragment : BaseMvpFragment<UnscheduledListPresenter>(), UnscheduledListContract.View {
    @Inject
    override lateinit var presenter: UnscheduledListPresenter

    private lateinit var autoAdapter: UnscheduleOpenAdapterAuto
    private lateinit var manualAdapter: UnscheduleOpenAdaptermanual
    private var listA = ArrayList<Unschedule>()
    private var listM = ArrayList<Unschedule>()
    private var pageA = 1
    private var pageM = 1

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }


    override fun onUnScheduleSetAuto(data: List<Unschedule>?) {
        dismissLoading()
        listA.clear()
        data?.let {
            listA.addAll(it)
        }
        val temp: ArrayList<Unschedule> = arrayListOf()
        listA.groupBy { listOf(it.type, it.location?.id) }.entries.map { res ->
            for (idx in res.value.indices){
                res.value[idx].isEnable = idx == 0
                temp.add(res.value[idx])
            }
        }
        listA = temp
        temp.clear()
        rvUnscheduledListAuto.adapter?.notifyDataSetChanged()
    }

    override fun onUnScheduleSetManual(data: List<Unschedule>?) {
        dismissLoading()
        listM.clear()
        data?.let {
            listM.addAll(it)
        }
        val temp: ArrayList<Unschedule> = arrayListOf()
        listM.groupBy { listOf(it.type, it.location?.id) }.entries.map { res ->
            for (idx in res.value.indices){
                res.value[idx].isEnable = idx == 0
                temp.add(res.value[idx])
            }
        }
        listM = temp
        temp.clear()
        rvUnscheduledListManual.adapter?.notifyDataSetChanged()
    }

    override fun setup() {
        initView()
        getListA(1)
        getListM(1)
    }
    private fun initView(){
        autoAdapter = UnscheduleOpenAdapterAuto(listA)
        rvUnscheduledListAuto.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = autoAdapter
            isNestedScrollingEnabled = false
        }
        autoAdapter.apply {
            setOnItemClickListener { _, _, position ->
                if (autoAdapter.data[position].isEnable){
                    if (autoAdapter.data[position].location?.lat != null && autoAdapter.data[position].location?.long != null) {
                        this@UnscheduledListFragment.context?.let {
                            startActivity(UnscheduleParentActivity.newInstance(it, autoAdapter.data[position]))
                        }
                    } else {
                        fragmentManager?.let {
                            ErrorDialogFragment.newInstance(Params.TAG_TAKE, getString(R.string.need_location_request), getString(R.string.ok_mengerti))
                                .show(it, ErrorDialogFragment::class.java.canonicalName)
                        }
                    }
                }
            }
            emptyView = LayoutInflater.from(context)
                .inflate(R.layout.empty_unschedule, rvUnscheduledListAuto, false)

        }
        cvLoadmoreA.setOnClickListener { onLoadMoreA() }

        manualAdapter = UnscheduleOpenAdaptermanual(listM)
        rvUnscheduledListManual.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = manualAdapter
            isNestedScrollingEnabled = false
        }
        manualAdapter.apply {
            setOnItemClickListener{ _, _, position ->
                if (manualAdapter.data[position].isEnable) {
                    if (manualAdapter.data[position].location?.lat != null && manualAdapter.data[position].location?.long != null) {
                        this@UnscheduledListFragment.context?.let {
                            startActivity(UnscheduleParentActivity.newInstance(it, manualAdapter.data[position]))
                        }
                    } else {
                        fragmentManager?.let {
                            ErrorDialogFragment.newInstance(Params.TAG_TAKE, getString(R.string.need_location_request), getString(R.string.ok_mengerti))
                                .show(it, ErrorDialogFragment::class.java.canonicalName)
                        }
                    }
                }
            }
            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_unschedule, rvUnscheduledListManual, false)
        }
        cvLoadmoreM.setOnClickListener { onLoadMoreM() }
    }
    private fun getListM(page: Int) {
        if (requireActivity().isNetworkAvailable()) {
            if (presenter.getUnsentManual().isNotEmpty()) {
                onUnScheduleSetManual(presenter.getOrignManual())
            } else {
                showLoading()
                presenter.getUnScheduleManual(
                    DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
                    DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5), requireActivity().isNetworkAvailable(), page)
            }
        } else {
            onUnScheduleSetManual(presenter.getOrignManual())
        }
    }

    private fun getListA(page: Int) {
        if (requireActivity().isNetworkAvailable()) {
            if (presenter.getUnsentAuto().isNotEmpty()) {
                onUnScheduleSetAuto(presenter.getOrignAuto())
            } else {
                showLoading()
                presenter.getUnScheduleAuto(
                    DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
                    DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5), requireActivity().isNetworkAvailable(), page)

            }
        } else {
            onUnScheduleSetAuto(presenter.getOrignAuto())
        }
    }

    private fun onLoadMoreA() {
        pageA++
        getListA(pageA)
    }

    private fun onLoadMoreM() {
        pageM++
        getListM(pageM)
    }

    override fun onUnScheduleSetAutoPager(data: UnscheduleListResponse) {
        if (pageA == 1)
            listA.clear()

        data.data?.list?.let {
            listA.addAll(it)
        }
        val temp: ArrayList<Unschedule> = arrayListOf()
        listA.groupBy { listOf(it.type, it.location?.id) }.entries.map { res ->
            for (idx in res.value.indices){
                res.value[idx].isEnable = idx == 0
                temp.add(res.value[idx])
            }
        }
        listA = temp
        temp.clear()
        presenter.archiveRepository.orignUnScheduleAuto = listA
        autoAdapter.notifyDataSetChanged()
        var loadMore = false
        data.data?.pagination?.let {
            pageA = it.current_page
            loadMore = pageA < it.totalPage
        }
        cvLoadmoreA.visibility = if (loadMore) View.VISIBLE else View.GONE
        dismissLoading()
    }

    override fun onUnScheduleSetManualPager(data: UnscheduleListResponse) {
        if (pageM == 1)
            listM.clear()

        data.data?.list?.let {
            listM.addAll(it)
        }
        val temp: ArrayList<Unschedule> = arrayListOf()
        listM.groupBy { listOf(it.type, it.location?.id) }.entries.map { res ->
            for (idx in res.value.indices){
                res.value[idx].isEnable = idx == 0
                temp.add(res.value[idx])
            }
        }
        listM = temp
        temp.clear()
        presenter.archiveRepository.orignUnSchedule = listM
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
            getListA(1)
            getListM(1)
        })
    }
}