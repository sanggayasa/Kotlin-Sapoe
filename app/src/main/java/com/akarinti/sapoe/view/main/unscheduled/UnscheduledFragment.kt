package com.akarinti.sapoe.view.main.unscheduled

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.event.UpdateStatusUnscheduleEvent
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.main.unscheduled.adapter.PageAdapterUn
import com.akarinti.sapoe.view.main.unscheduled.rute.RuteActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_unscheduled.*
import kotlinx.android.synthetic.main.header_default.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class UnscheduledFragment : BaseMvpFragment<UnscheduledPresenter>(), UnscheduledContract.View {
    override fun gotoNextPage() {

    }

    @Inject
    override lateinit var presenter: UnscheduledPresenter

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
        tvTitle.setText(R.string.rute_tak_terjadwal)

        vpUnscheduled.apply {
            adapter=PageAdapterUn(childFragmentManager)
            setSwipePagingEnabled(true)
        }
        tabUnscheduled.setupWithViewPager(vpUnscheduled)
    }
    private fun initAction(){
        btnAddRute.setOnClickListener {
            startActivity(activity?.intentFor<RuteActivity>())
        }
        tvRefreshTop.setOnClickListener {
            RxBus.publish(UpdateStatusUnscheduleEvent())
        }
    }

    override fun getLayout(): Int = R.layout.fragment_unscheduled
}