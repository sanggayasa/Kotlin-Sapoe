package com.akarinti.sapoe.view.main.visit

import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.event.UpdateStatusScheduledEvent
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.main.visit.adapter.PageAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_visit.*
import kotlinx.android.synthetic.main.header_default.*
import javax.inject.Inject

class VisitFragment : BaseMvpFragment<VisitPresenter>(), VisitContract.View {

    @Inject
    override lateinit var presenter: VisitPresenter

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

        ivLogo.visibility=View.GONE
        tvRefreshTop.visibility=View.VISIBLE
        tvTitle.visibility=View.VISIBLE
        tvContent.visibility = View.GONE
        tvTitle.setText(R.string.rute_kunjungan)
        vpKunjungan.apply {
            adapter=PageAdapter(childFragmentManager)
            setSwipePagingEnabled(true)
        }
        tabKunjungan.setupWithViewPager(vpKunjungan)
    }
    private fun initAction(){
        tvRefreshTop.setOnClickListener {
            RxBus.publish(UpdateStatusScheduledEvent())
        }
    }
    override fun getLayout(): Int = R.layout.fragment_visit
}