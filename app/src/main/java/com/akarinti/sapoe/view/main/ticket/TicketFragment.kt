package com.akarinti.sapoe.view.main.ticket

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.event.GotoTicketEvent
import com.akarinti.sapoe.event.TicketRefreshEvent
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.ticket.adapter.PageAdapterTicket
import com.akarinti.sapoe.view.main.ticket.add.TiketActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.header_default.*
import javax.inject.Inject

class TicketFragment : BaseMvpFragment<TicketPresenter>(), TicketContract.View {

    @Inject
    override lateinit var presenter: TicketPresenter

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }
    override fun setup() {
        initView()
        initAction()
    }
    private fun initView(){
        ivLogo.visibility= View.GONE
        tvRefreshTop.visibility= View.VISIBLE
        tvTitle.visibility= View.VISIBLE
        tvContent.visibility = View.GONE
        tvTitle.setText(R.string.ticket)
        vpTicket.apply {
            adapter= PageAdapterTicket(childFragmentManager)
            setSwipePagingEnabled(true)
        }
        tabTicket.setupWithViewPager(vpTicket)
    }
    private fun initAction(){
        btnAddTicket.setOnClickListener {
            if (requireActivity().isNetworkAvailable()) {
                this@TicketFragment.activity?.let {
                    TiketActivity.newInstance(it, "", "","","")
                }
            } else {
                fragmentManager?.let {
                    ErrorDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.network_offline), getString(R.string.tutup))
                        .show(it, ErrorDialogFragment::class.java.canonicalName)
                }
            }
        }
        tvRefreshTop.setOnClickListener {
            RxBus.publish(TicketRefreshEvent())
        }
        addUiSubscription(RxBus.listen(GotoTicketEvent::class.java).subscribe {
            vpTicket.currentItem = 0
        })
    }

    override fun getLayout(): Int = R.layout.fragment_ticket
}