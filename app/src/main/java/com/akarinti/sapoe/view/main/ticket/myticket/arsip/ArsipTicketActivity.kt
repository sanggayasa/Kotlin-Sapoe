package com.akarinti.sapoe.view.main.ticket.myticket.arsip

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.extension.rounded
import com.akarinti.sapoe.model.ticket.Ticket
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.main.ticket.adapter.ArsipTicketAdapter
import com.akarinti.sapoe.view.main.ticket.myticket.arsipdetail.ArsipDetailActivity
import dagger.android.AndroidInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_arsip_ticket.*
import kotlinx.android.synthetic.main.header_default.tvTitle
import kotlinx.android.synthetic.main.toolbar_navigation.*
import javax.inject.Inject


class ArsipTicketActivity : BaseMvpActivity<ArsipTicketPresenter>(),
    ArsipTicketContract.View {
    @Inject
    override lateinit var presenter: ArsipTicketPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        getList()
        initView()
    }
    private var list = ArrayList<Ticket>()

    private fun initView(){
        tvTitle.setText(R.string.arsip_tiket)
        val visitListadapter = ArsipTicketAdapter(list)
        rvTiketArsip.apply {
            layoutManager = LinearLayoutManager(this@ArsipTicketActivity)
            adapter = visitListadapter
            isNestedScrollingEnabled = false
        }
        visitListadapter.apply {
            setOnItemClickListener { _, _, pos ->
                ArsipDetailActivity.newInstance(this@ArsipTicketActivity, this.data[pos])
            }
            emptyView = LayoutInflater.from(this@ArsipTicketActivity).inflate(R.layout.empty_schedule, rvTiketArsip, false)
        }
        ivBack.setOnClickListener { onBackPressed() }

    }
    private fun getList() {
        showLoading()
        presenter.getArsipTicket(
            DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].rounded(5),
            DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].rounded(5))
    }

    override fun getLayout(): Int = R.layout.activity_arsip_ticket
    override fun onArsipTicketSet(data: List<Ticket>?) {
        dismissLoading()
        list.clear()
        data?.let {
            list.addAll(it)
        }
        rvTiketArsip.adapter?.notifyDataSetChanged()
    }
}