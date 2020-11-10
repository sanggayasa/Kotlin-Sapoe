package com.akarinti.sapoe.view.main.ticket.ticketall

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.data.response.TicketListResponse
import com.akarinti.sapoe.event.TicketRefreshEvent
import com.akarinti.sapoe.extension.rounded
import com.akarinti.sapoe.model.ticket.Ticket
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.ticket.adapter.TicketListAdapter
import com.akarinti.sapoe.view.main.ticket.add.info.InfoTiketActivity
import dagger.android.support.AndroidSupportInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.fragment_ticket_list.*
import javax.inject.Inject

class TicketListFragment : BaseMvpFragment<TicketListPresenter>(), TicketListContract.View {

    @Inject
    override lateinit var presenter: TicketListPresenter

    private var list = ArrayList<Ticket>()
    private var page = 1
    private lateinit var visitListadapter: TicketListAdapter

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }
    override fun setup() {
        initView()
        initAction()
        getList(1)
    }

    private fun initAction() {
        searchTiket.afterTextChangedDelayed {
            showLoading()
            presenter.getTicket(
                it,
                DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
                DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5),
                "",
                1,
                100
            )
        }
        cvLoadmore.setOnClickListener { loadMore() }
    }

    private fun initView(){
        tvArsip.visibility= View.GONE
        searchTiket.visibility= View.VISIBLE
        visitListadapter = TicketListAdapter(list)
        rvTiketList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = visitListadapter
            isNestedScrollingEnabled = false
        }
        visitListadapter.apply {
            setOnItemClickListener { _, _, pos ->
                if (visitListadapter.data[pos].location?.lat != null && visitListadapter.data[pos].location?.long != null) {
                    this@TicketListFragment.activity?.let {
                        InfoTiketActivity.newInstance(it, visitListadapter.data[pos])
                    }
                } else {
                    fragmentManager?.let {
                        ErrorDialogFragment.newInstance(Params.TAG_TAKE, getString(R.string.need_location_request), getString(R.string.ok_mengerti))
                            .show(it, ErrorDialogFragment::class.java.canonicalName)
                    }
                }
            }
            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_schedule, rvTiketList, false)
        }
    }

    private fun getList(page: Int) {
        showLoading()
        presenter.getTicket("",
            DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].rounded(5),
            DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].rounded(5),
            "", page)
    }

    private fun loadMore() {
        page++
        getList(page)
    }

    override fun onTicketSet(data: TicketListResponse) {
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

    private fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            var timer: CountDownTimer? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                timer?.cancel()
                timer = object : CountDownTimer(1000, 1500) {
                    override fun onTick(millisUntilFinished: Long) {

                    }
                    override fun onFinish() {
                        afterTextChanged.invoke(editable.toString())
                    }
                }.start()
                dismissLoading()
            }
        })
    }

    override fun getLayout(): Int = R.layout.fragment_ticket_list

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(TicketRefreshEvent::class.java).subscribe {
            getList(1)
        })
    }
}