package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.child

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.event.GotoTicketEvent
import com.akarinti.sapoe.event.RouteFethUpdateEvent
import com.akarinti.sapoe.event.TicketRefreshEvent
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.extension.rounded
import com.akarinti.sapoe.model.Route
import com.akarinti.sapoe.model.questionnaire.*
import com.akarinti.sapoe.model.ticket.Ticket
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.ticket.adapter.TemuanTicketAdapter
import com.akarinti.sapoe.view.main.ticket.add.TiketActivity
import com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.question.UQuestionActivity
import com.akarinti.sapoe.view.main.visit.visit_parent.adapter.QuestionOfflineAdapter
import dagger.android.AndroidInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class UnscheduleChildActivity: BaseMvpActivity<UnscheduleChildPresenter>(), UnscheduleChildContract.View, ConfirmDialogFragment.Listener {

    companion object {
        fun newInstance(activity: Activity, unscheduleID: String?, unscheduleType: String?, selectedChildIndex: Int?) {
            activity.startActivityForResult(activity.intentFor<UnscheduleChildActivity>(
                Params.BUNDLE_UNSCHEDULE_ID to unscheduleID,
                Params.BUNDLE_UNSCHEDULE_TYPE to unscheduleType,
                Params.BUNDLE_UNSCHEDULE_CHILD_IDX to selectedChildIndex
            ), CodeIntent.OPEN_QUESTION)
        }
    }

    private var unscheduleID: String? = null
    private var unscheduleType: String? = null
    private var itemId: String? = null
    private var childIndex: Int = -1
    private var curPos: Int = -1

    private lateinit var dataAc: ParentAcOffline
    private lateinit var dataNb: ParentNbOffline
    private lateinit var dataCl: ParentCleanOffline
    private lateinit var dataMcds: ParentOtherOffline
    private lateinit var dataQc: ParentOtherOffline
    private lateinit var adapterQuestion: QuestionOfflineAdapter
    private var list = ArrayList<Ticket>()
    private lateinit var route: Route

    @Inject
    override lateinit var presenter: UnscheduleChildPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initBundle()
        initView()
        initAction()
        initData()
        initTicket()
    }

    override fun getLayout(): Int = R.layout.activity_info

    private fun initBundle() {
        intent?.extras?.let {
            unscheduleID = it.getString(Params.BUNDLE_UNSCHEDULE_ID)
            unscheduleType = it.getString(Params.BUNDLE_UNSCHEDULE_TYPE)
            childIndex = it.getInt(Params.BUNDLE_UNSCHEDULE_CHILD_IDX)
        }

    }

    private fun initView() {
        tvTitle.text = getString(R.string.kunjungan_info)
        btnAction.text = getString(R.string.simpan)
    }

    private fun initAction() {
        btnAction.setOnClickListener {
            if (null != unscheduleID && null != unscheduleType && childIndex != -1) {
                when (unscheduleType) {
                    Params.TYPE_AC -> {
                        val orignData: MutableList<ParentAcOffline> = (presenter.inprogressRepository.unscheduledAc ?: ArrayList()).toMutableList()
                        orignData.remove(dataAc)
                        dataAc.itemList?.get(childIndex)?.isDone = true
                        orignData.add(dataAc)
                        presenter.inprogressRepository.unscheduledAc = orignData
                    }
                    Params.TYPE_NEONBOX -> {
                        val orignData: MutableList<ParentNbOffline> = (presenter.inprogressRepository.unscheduledNb ?: ArrayList()).toMutableList()
                        orignData.remove(dataNb)
                        dataNb.itemList?.get(childIndex)?.isDone = true
                        orignData.add(dataNb)
                        presenter.inprogressRepository.unscheduledNb = orignData
                    }
                    Params.TYPE_CLEAN -> {
                        val orignData: MutableList<ParentCleanOffline> = (presenter.inprogressRepository.unscheduledCl ?: ArrayList()).toMutableList()
                        orignData.remove(dataCl)
                        dataCl.itemList?.get(childIndex)?.isDone = true
                        orignData.add(dataCl)
                        presenter.inprogressRepository.unscheduledCl = orignData
                    }
                    Params.TYPE_MCDS -> {
                        val orignData: MutableList<ParentOtherOffline> = (presenter.inprogressRepository.unscheduledMcds ?: ArrayList()).toMutableList()
                        orignData.remove(dataMcds)
                        dataMcds.itemList?.get(childIndex)?.isDone = true
                        orignData.add(dataMcds)
                        presenter.inprogressRepository.unscheduledMcds = orignData
                    }
                    Params.TYPE_QC -> {
                        val orignData: MutableList<ParentOtherOffline> = (presenter.inprogressRepository.unscheduledQc ?: ArrayList()).toMutableList()
                        orignData.remove(dataQc)
                        dataQc.itemList?.get(childIndex)?.isDone = true
                        orignData.add(dataQc)
                        presenter.inprogressRepository.unscheduledQc = orignData
                    }
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
        tvTemuan.setOnClickListener {
            if(isNetworkAvailable())
                TiketActivity.newInstance(this, unscheduleID, Params.CATEGORY_UNSCHEDULED, unscheduleType, itemId, route)
            else
                ErrorDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.network_offline), getString(R.string.tutup))
                    .show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
        }
        btGoTicket.setOnClickListener {
            RxBus.publish(GotoTicketEvent())
            finish()
        }
        btAddTicket.setOnClickListener {
            if(isNetworkAvailable())
                TiketActivity.newInstance(this, unscheduleID, Params.CATEGORY_UNSCHEDULED, unscheduleType, itemId, route)
            else
                ErrorDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.network_offline), getString(R.string.tutup))
                    .show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
        }
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun initData() {
        if (null != unscheduleID && null != unscheduleType && childIndex != -1) {
            var isDone = false
            var isFinish = false
            when (unscheduleType) {
                Params.TYPE_AC -> {
                    dataAc = presenter.inprogressRepository.unscheduledAc?.find { it.id == unscheduleID }!!
                    if (null != dataAc) {
                        tvName.text = String.format(
                            getString(R.string.name_ac_fmt),
                            dataAc.location?.name ?: "-",
                            childIndex + 1
                        )
                        tvInfo.text = String.format(
                            getString(R.string.serial_no_fmt),
                            dataAc.itemList?.get(childIndex)?.indoorSerialNumber?: "-"
                        )
                        initQuestionList(dataAc.itemList?.get(childIndex)?.formList)
                        isDone = (dataAc.itemList?.get(childIndex)?.formList?.all { it.isDone == true } == true)
                        isFinish = dataAc.itemList?.get(childIndex)?.isDone == true
                        itemId = dataAc.itemList?.get(childIndex)?.id
                        route = Route(dataAc.location?.id, dataAc.location?.name, dataAc.location?.address, dataAc.location?.lat, dataAc.location?.long)
                    }
                }
                Params.TYPE_NEONBOX -> {
                    dataNb = presenter.inprogressRepository.unscheduledNb?.find { it.id == unscheduleID }!!
                    if (null != dataNb) {
                        tvName.text = String.format(
                            getString(R.string.name_nb_fmt),
                            dataNb.location?.name ?: "-",
                            childIndex + 1
                        )
                        tvInfo.text = String.format(getString(R.string.tipe_nb_fmt), dataNb.itemList?.get(childIndex)?.type ?: "-")
                        initQuestionList(dataNb.itemList?.get(childIndex)?.formList)
                        isDone = (dataNb.itemList?.get(childIndex)?.formList?.all { it.isDone == true } == true)
                        isFinish = dataNb.itemList?.get(childIndex)?.isDone == true
                        itemId = dataNb.itemList?.get(childIndex)?.id
                        route = Route(dataNb.location?.id, dataNb.location?.name, dataNb.location?.address, dataNb.location?.lat, dataNb.location?.long)
                    }
                }
                Params.TYPE_CLEAN -> {
                    dataCl = presenter.inprogressRepository.unscheduledCl?.find { it.id == unscheduleID }!!
                    if (null != dataCl) {
                        tvName.text = String.format(
                            getString(R.string.name_cl_fmt),
                            dataCl.location?.name ?: "-",
                            childIndex + 1
                        )
                        tvInfo.text = String.format(getString(R.string.wsid_fmt), dataCl.itemList?.get(childIndex)?.wsid ?: "-")
                        initQuestionList(dataCl.itemList?.get(childIndex)?.formList)
                        isDone = (dataCl.itemList?.get(childIndex)?.formList?.all { it.isDone == true } == true)
                        isFinish = dataCl.itemList?.get(childIndex)?.isDone == true
                        itemId = dataCl.itemList?.get(childIndex)?.id
                        route = Route(dataCl.location?.id, dataCl.location?.name, dataCl.location?.address, dataCl.location?.lat, dataCl.location?.long)
                    }
                }
                Params.TYPE_MCDS -> {
                    dataMcds = presenter.inprogressRepository.unscheduledMcds?.find { it.id == unscheduleID }!!
                    if (null != dataMcds) {
                        tvName.text = String.format(
                            getString(R.string.name_cl_fmt),
                            dataMcds.location?.name ?: "-",
                            childIndex + 1
                        )
                        tvInfo.text = String.format(getString(R.string.wsid_fmt), dataMcds.itemList?.get(childIndex)?.wsid ?: "-")
                        initQuestionList(dataMcds.itemList?.get(childIndex)?.formList)
                        isDone = (dataMcds.itemList?.get(childIndex)?.formList?.all { it.isDone == true } == true)
                        isFinish = dataMcds.itemList?.get(childIndex)?.isDone == true
                        itemId = dataMcds.itemList?.get(childIndex)?.id
                        route = Route(dataMcds.location?.id, dataMcds.location?.name, dataMcds.location?.address, dataMcds.location?.lat, dataMcds.location?.long)
                    }
                }
                Params.TYPE_QC -> {
                    dataQc = presenter.inprogressRepository.unscheduledQc?.find { it.id == unscheduleID }!!
                    if (null != dataQc) {
                        tvName.text = String.format(
                            getString(R.string.name_cl_fmt),
                            dataQc.location?.name ?: "-",
                            childIndex + 1
                        )
                        tvInfo.text = String.format(getString(R.string.wsid_fmt), dataQc.itemList?.get(childIndex)?.wsid ?: "-")
                        initQuestionList(dataQc.itemList?.get(childIndex)?.formList)
                        isDone = (dataQc.itemList?.get(childIndex)?.formList?.all { it.isDone == true } == true)
                        isFinish = dataQc.itemList?.get(childIndex)?.isDone == true
                        itemId = dataQc.itemList?.get(childIndex)?.id
                        route = Route(dataQc.location?.id, dataQc.location?.name, dataQc.location?.address, dataQc.location?.lat, dataQc.location?.long)
                    }
                }
            }
            btnAction.isEnabled = isDone
//            tvStatus.visibility = if (isFinish) View.INVISIBLE else View.VISIBLE
            ivFinish.visibility = if (isFinish) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun initQuestionList(data: List<QuestionOffline>?) {
        adapterQuestion = QuestionOfflineAdapter(data?: listOf())
        rvQuestion.apply {
            layoutManager = LinearLayoutManager(this@UnscheduleChildActivity)
            adapter = adapterQuestion
            isNestedScrollingEnabled = false
        }
        adapterQuestion.setOnItemClickListener { _, _, position ->
            curPos = position
            UQuestionActivity.newInstance(this, unscheduleID, unscheduleType, position, childIndex)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CodeIntent.OPEN_QUESTION && resultCode == Activity.RESULT_OK) {
            showLoading()
            initData()
            initTicket()
            if (curPos != -1 && adapterQuestion.data.size > curPos+1) {
                for (i in curPos until adapterQuestion.data.size) {
                    if (adapterQuestion.data[i].isDone != true) {
                        curPos = i
                        UQuestionActivity.newInstance(this, unscheduleID, unscheduleType, curPos, childIndex)
                        break
                    }
                }
            }
            if (curPos != -1 && curPos+1 == adapterQuestion.data.size && isNetworkAvailable()) {
                curPos = -1
                ConfirmDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.lapor_kunjungan), getString(R.string.lapor), getString(R.string.lewati))
                    .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
            }
        }
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
        if (tag == Params.TAG_CREATE)
            TiketActivity.newInstance(this, unscheduleID, Params.CATEGORY_UNSCHEDULED, unscheduleType, itemId, route)
    }

    override fun onConfirmDialogDismissed() {
    }

    private fun initTicket(){
        showLoading()
        presenter.getTicket(
            DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].rounded(5),
            DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].rounded(5),
            unscheduleID.toString(),
            Params.CATEGORY_UNSCHEDULED)
    }
    override fun onTicketSet(data: List<Ticket>?) {
        dismissLoading()
        list.clear()
        data?.let {
            list.addAll(it)
            when {
                it.size!=0 -> {
                    tabPartsTicket.visibility=View.VISIBLE
                    ivTicketNum.visibility=View.VISIBLE
                    ivTicketNum.text=it.size.toString()
                }
                else -> {
                    tabPartsTicket.visibility=View.GONE
                    ivTicketNum.visibility=View.GONE

                }
            }
            val visitListadapter = TemuanTicketAdapter(list)
            rvTicketList.apply {
                layoutManager = LinearLayoutManager(this@UnscheduleChildActivity)
                adapter = visitListadapter
                isNestedScrollingEnabled = false
            }
        }

        rvTicketList.adapter?.notifyDataSetChanged()
        RxBus.publish(RouteFethUpdateEvent())
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(TicketRefreshEvent::class.java).subscribe {
            initData()
            initTicket()
        })
    }
}