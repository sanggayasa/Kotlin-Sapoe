package com.akarinti.sapoe.view.main.visit.onprogress

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.data.response.ScheduleListResponse
import com.akarinti.sapoe.event.UpdateStatusScheduledEvent
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.extension.rounded
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.visit.adapter.ScheduleOpenAdapter
import com.akarinti.sapoe.view.main.visit.visit_parent.VisitParentActivity
import dagger.android.support.AndroidSupportInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.fragment_visit_list.*
import javax.inject.Inject

class VisitListFragment : BaseMvpFragment<VisitListPresenter>(), VisitListContract.View {

    @Inject
    override lateinit var presenter: VisitListPresenter

    private lateinit var visitListadapter: ScheduleOpenAdapter
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
        getList(1)
    }

    private fun initView() {
        visitListadapter = ScheduleOpenAdapter(list)
        rvVisitList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = visitListadapter
            isNestedScrollingEnabled = false
        }
        visitListadapter.apply {
            setOnItemClickListener { _, _, pos ->
                if (visitListadapter.data[pos].isEnable) {
                    if (visitListadapter.data[pos].location?.lat != null && visitListadapter.data[pos].location?.long != null) {
                        this@VisitListFragment.context?.let {
                            startActivity(VisitParentActivity.newInstance(it, visitListadapter.data[pos]))
                        }
                    } else {
                        fragmentManager?.let {
                            ErrorDialogFragment.newInstance(Params.TAG_TAKE, getString(R.string.need_location_request), getString(R.string.ok_mengerti))
                                .show(it, ErrorDialogFragment::class.java.canonicalName)
                        }
                    }
                }
            }
            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_schedule, rvVisitList, false)
        }
        cvLoadmore.setOnClickListener { onLoadMore() }
    }

    private fun getList(page: Int) {
        if (requireActivity().isNetworkAvailable()) {
            if (presenter.getUnsent().isNotEmpty()){
                onScheduleSetOffline(presenter.getOrign())
            } else {
                showLoading()
                presenter.getSchedule(DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
                    DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5),
                    requireActivity().isNetworkAvailable(), page)
            }
        } else {
            onScheduleSetOffline(presenter.getOrign())
        }
    }

    private fun onLoadMore() {
        page++
        getList(page)
    }

    override fun onScheduleSetOffline(data: List<Schedule>?) {
        dismissLoading()
        list.clear()
        data?.let {
            list.addAll(it)
        }
        val temp: ArrayList<Schedule> = arrayListOf()
        list.groupBy { listOf(it.type, it.location?.id) }.entries.map { res ->
            for (idx in res.value.indices){
                res.value[idx].isEnable = idx == 0
                temp.add(res.value[idx])
            }
        }
        list = temp
        temp.clear()
        rvVisitList.adapter?.notifyDataSetChanged()
    }

    override fun onScheduleSetPager(data: ScheduleListResponse) {
        if (page == 1)
            list.clear()

        data.data?.list?.let {
            list.addAll(it)
        }
        val temp: ArrayList<Schedule> = arrayListOf()
        list.groupBy { listOf(it.type, it.location?.id) }.entries.map { res ->
            for (idx in res.value.indices){
                res.value[idx].isEnable = idx == 0
                temp.add(res.value[idx])
            }
        }
        list = temp
        temp.clear()
        presenter.archiveRepository.orignSchedule = list
        visitListadapter.notifyDataSetChanged()
        var loadMore = false
        data.data?.pagination?.let {
            page = it.current_page
            loadMore = page < it.totalPage
        }
        cvLoadmore.visibility = if (loadMore) View.VISIBLE else View.GONE
        dismissLoading()
    }

    override fun getLayout(): Int = R.layout.fragment_visit_list

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(UpdateStatusScheduledEvent::class.java).subscribe {
            page = 1
            getList(1)
        })
    }
}