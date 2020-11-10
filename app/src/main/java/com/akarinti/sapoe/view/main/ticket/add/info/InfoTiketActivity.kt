package com.akarinti.sapoe.view.main.ticket.add.info

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.body.UpdateTicketBody
import com.akarinti.sapoe.event.SparepartRefreshEvent
import com.akarinti.sapoe.extension.getLat
import com.akarinti.sapoe.extension.getLong
import com.akarinti.sapoe.extension.openMap
import com.akarinti.sapoe.model.ticket.SparepartList
import com.akarinti.sapoe.model.ticket.Ticket
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.GlideApp
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.akarinti.sapoe.view.main.MainActivity
import com.akarinti.sapoe.view.main.ticket.adapter.SparepartsAdapter
import com.akarinti.sapoe.view.main.ticket.add.foto.FotoAfterActivity
import com.akarinti.sapoe.view.main.ticket.add.foto.FotoBeforeActivity
import com.akarinti.sapoe.view.main.ticket.add.spareparts.SparepartsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_info_tiket.*
import kotlinx.android.synthetic.main.button_navigation.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import java.util.*
import javax.inject.Inject

class InfoTiketActivity : BaseMvpActivity<InfoTiketPresenter>(), InfoTiketContract.View,
    ConfirmDialogFragment.Listener, FinisDialogFragment.Listener, ErrorDialogFragment.Listener {

    companion object {
        fun newInstance(activity: Activity, ticket: Ticket) {
            activity.startActivity(activity.intentFor<InfoTiketActivity>(Params.BUNDLE_TICKET to ticket))
        }
    }

    @Inject
    override lateinit var presenter: InfoTiketPresenter
    private var ticket: Ticket? = null
    private var imageBefore: String = ""
    private var imageAfter: String = ""
    private var list = ArrayList<SparepartList>()
    private lateinit var visitListadapter: SparepartsAdapter
    private var sparepartID: String = ""
    private var cityID: String = ""

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }
    private fun initBundle() {
        intent?.extras?.let {
            ticket = it.getParcelable(Params.BUNDLE_TICKET)?: null
        }

        ticket?.id?.let {
            showLoading()
            presenter.getTicketDetail(it)
        }

    }
    override fun setup() {
        //if (isMockAppInstalled()) return
        initView()
        initBundle()
        initAction()
        loadLocalData()
    }

    override fun getLayout(): Int = R.layout.activity_info_tiket

    private fun initView() {
        tvTitle.text = getString(R.string.info_tiket)
        ivMap.visibility= View.VISIBLE

        visitListadapter = SparepartsAdapter(list, true)
        rvSparepartsAdd.apply {
            layoutManager = LinearLayoutManager(this@InfoTiketActivity)
            adapter = visitListadapter
            isNestedScrollingEnabled = false
        }
        visitListadapter.setOnItemChildClickListener { _, v, pos ->
            when (v.id) {
                R.id.ivDelete -> {
                    if (list.size > pos) {
                        sparepartID =  list[pos].id?:""
                        showDeletePopup()
                    }
                }
            }
        }
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
        btnSimpan.setOnClickListener {
            if(btnSimpan.text == getString(R.string.ambil_tiket))
                showConfirmPopup()
            else
                showCloseTiketPopup()
        }
        tabImageBefore.setOnClickListener {
            startActivityForResult(FotoBeforeActivity.newInstance(this,ticket?.id.toString(), list.size, cityID), CodeIntent.OPEN_QUESTION)
        }
        tabImageAfter.setOnClickListener {
            startActivityForResult(FotoAfterActivity.newInstance(this,ticket?.id.toString()), CodeIntent.OPEN_QUESTION)
        }
        tabFinding.setOnClickListener {
            if(tabPartsList.visibility==View.GONE) {
                startActivity(SparepartsActivity.newInstance(this, ticket?.id.toString(), list.size, cityID))
            }
        }
        btAddTicket.setOnClickListener {
            startActivity(SparepartsActivity.newInstance(this, ticket?.id.toString(), list.size, cityID))
        }
        ivMap.setOnClickListener {
            openMap(ticket?.location?.lat?: 0.0 , ticket?.location?.long?: 0.0)
        }
    }

    private fun loadLocalData() {
        presenter.getRepository().find { it.orderID == ticket?.id }?.let {
            if (it.imageBefore != null) {
                ivDoneBefore.setImageResource(R.drawable.ic_complete)
                ivDoneBefore.tag = R.drawable.ic_complete
                this.imageBefore = it.imageBefore!!
            }
            if (it.imageAfter != null) {
                ivDoneAfter.setImageResource(R.drawable.ic_complete)
                ivDoneAfter.tag = R.drawable.ic_complete
                this.imageAfter = it.imageAfter!!
            }
            checkButton()
        }
    }

    private fun showConfirmPopup() {
        ConfirmDialogFragment.newInstance(Params.TAG_TAKE_TICKET, getString(R.string.yakin_ambil_tiket), getString(R.string.ambil_tiket), getString(R.string.batal))
            .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
    }
    private fun showCloseTiketPopup() {
        ConfirmDialogFragment.newInstance(Params.TAG_CLOSE_TICKET, getString(R.string.yakin_tutup_tiket), getString(R.string.tutup_tiket), getString(R.string.batal))
            .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
    }

    private fun showDeletePopup() {
        ConfirmDialogFragment.newInstance(Params.TAG_REMOVE, getString(R.string.yakin_delete_sparepart), getString(R.string.ya), getString(R.string.batal))
            .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
    }

    private fun checkButton() {
        if(btnSimpan.text == getString(R.string.tutup_tiket))
            btnSimpan.isEnabled = (ivDoneBefore.tag == R.drawable.ic_complete) && (ivDoneAfter.tag == R.drawable.ic_complete)
    }

    override fun onFinishDialogBtnPressed() {
        startActivity(intentFor<MainActivity>())
        finishAffinity()
    }

    override fun onCancelDialogBtnPressed(tag: String?) {}

    override fun onConfirmDialogBtnPressed(tag: String?) {
        tag?.let {
            ticket?.id?.let {id ->
                when (it) {
                    Params.TAG_TAKE_TICKET -> {
                        showLoading()
                        presenter.takeTicket(id, getLat(), getLong())
                    }
                    Params.TAG_CLOSE_TICKET -> {
                        showLoading()
                        presenter.closeTicket(id, UpdateTicketBody(
                            imageBefore,
                            imageAfter,
                            "closed",
                            Calendar.getInstance().timeInMillis/1000L,
                            presenter.profileRepository.profile?.id
                        ), getLat(), getLong())
                    }
                    Params.TAG_REMOVE -> {
                        showLoading()
                        presenter.deleteSparepart(sparepartID, id)
                    }
                    else -> {}
                }
            }
        }
    }


    override fun onConfirmDialogDismissed() {}

    override fun onTicketDetail(data: Ticket?, agentID: String) {
        data?.let {
            cityID = it.location?.regencyId ?: ""
            tvPlaceParent.text = it.location?.name
            tvAddress.text = it.location?.address
            spKategori.setText(it.categoryValue)
            spType.setText(it.ticketType?:"-")
            spSla.setText(it.sla?.description?:"-")
            etKeterangan.setText(it.description?:"-")
            tvReporter.text = it.createdBy?.name?: "-"
            tvAssign.text = it.agent?.name?: "-"
            GlideApp.with(this)
                .load(it.image)
                .into(ivResult)

            if (null == it.closedBy) {
                if (null != it.agent && it.agent.id == presenter.profileRepository.profile!!.id) {
                    containerButton.visibility = View.VISIBLE
                    btnSimpan.text = getString(R.string.tutup_tiket)
                    tabOption.visibility = View.VISIBLE
                    btnSimpan.isEnabled = false
                } else {
                    btnSimpan.text = getString(R.string.ambil_tiket)
                }
            } else {
                containerButton.visibility = View.GONE
            }
            list.clear()
            list.addAll(it.sparepartList?: listOf())
            visitListadapter.notifyDataSetChanged()

            val isNotEmpty = list.size > 0
            ivFinding.visibility = if (isNotEmpty) View.GONE else View.VISIBLE
            ivTicketNum.visibility = if (isNotEmpty) View.VISIBLE else View.GONE
            ivTicketNum.text = "${list.size}"
            tabPartsList.visibility = if (isNotEmpty) View.VISIBLE else View.GONE
        }
        checkButton()
        dismissLoading()
    }

    override fun onCloseTicket() {
        dismissLoading()
        FinisDialogFragment.newInstance(getString(R.string.sukses_tutup), getString(R.string.kembali_beranda),R.drawable.popup_success)
            .show(supportFragmentManager, FinisDialogFragment::class.java.canonicalName)
    }

    override fun errorScreen(message: String?, code: Int?) {
        if (code == 400) {
            ErrorDialogFragment.newInstance(
                Params.TAG_TAKE, message?:"-", getString(R.string.ok_mengerti)
            ).show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
        } else {
            super.errorScreen(message, code)
        }
    }

    override fun onErrorDialogBtnPressed(tag: String?) {
        finish()
    }

    override fun onErrorDialogDismissed(tag: String?) {
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(SparepartRefreshEvent::class.java).subscribe {
            ticket?.id?.let {
                showLoading()
                presenter.getTicketDetail(it)
            }
            loadLocalData()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CodeIntent.OPEN_QUESTION && resultCode == Activity.RESULT_OK) {
            ticket?.id?.let {
                showLoading()
                presenter.getTicketDetail(it)
            }
            loadLocalData()
        }
    }
}