package com.akarinti.sapoe.view.main.ticket.myticket.arsipdetail

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseActivity
import com.akarinti.sapoe.model.ticket.Ticket
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import com.akarinti.sapoe.view.main.ticket.adapter.SparepartsAdapter
import kotlinx.android.synthetic.main.activity_info_tiket.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor

class ArsipDetailActivity : BaseActivity() {

    companion object {
        fun newInstance(activity: Activity, ticket: Ticket) {
            activity.startActivity(activity.intentFor<ArsipDetailActivity>(Params.BUNDLE_TICKET to ticket))
        }
    }

    override fun injectView() {
    }

    override fun setup() {
        initBundle()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_info_tiket

    private fun initBundle() {
        intent?.extras?.let { it ->
            initView(it.getParcelable(Params.BUNDLE_TICKET))
        }
    }

    private fun initView(it: Ticket?) {
        containerButton.visibility = View.GONE
        tvTitle.text = getString(R.string.info_tiket)
        if (null != it) {
            tvPlaceParent.text = it.location?.name
            tvAddress.text = it.location?.address
            spKategori.setText(it.categoryType)
            spType.setText(it.ticketType?:"-")
            spSla.setText(it.sla?.description?:"-")
            etKeterangan.setText(it.description?:"-")
            tvReporter.text = it.createdBy?.name?: "-"
            tvAssign.text = it.agent?.name?: "-"
            GlideApp.with(this)
                .load(it.image)
                .into(ivResult)

            tabOption.visibility = View.VISIBLE
            btAddTicket.visibility = View.GONE
            tabFinding.visibility = View.GONE
            val visitListadapter = SparepartsAdapter(it.sparepartList?: listOf())
            rvSparepartsAdd.apply {
                layoutManager = LinearLayoutManager(this@ArsipDetailActivity)
                adapter = visitListadapter
                isNestedScrollingEnabled = false
            }
            when {
                it.sparepartList.isNullOrEmpty() -> {
                    ivFinding.visibility=View.VISIBLE
                    ivTicketNum.visibility=View.GONE
                    tabPartsList.visibility=View.GONE
                }
                else -> {
                    ivFinding.visibility=View.GONE
                    ivTicketNum.visibility=View.VISIBLE
                    ivTicketNum.text=it.sparepartList.size.toString()
                    tabPartsList.visibility=View.VISIBLE
                }
            }
            ivDoneBefore.setImageResource(R.drawable.ic_complete)
            ivDoneAfter.setImageResource(R.drawable.ic_complete)
            tabImageBefore.setOnClickListener { _ ->
                PhotoDetailActivity.newInstance(this, it.imageBefore?:"", getString(R.string.image_before), it.location?.lat, it.location?.long)
            }
            tabImageAfter.setOnClickListener { _ ->
                PhotoDetailActivity.newInstance(this, it.imageAfter?:"", getString(R.string.image_after), it.location?.lat, it.location?.long)
            }

        } else {
            onBackPressed()
        }
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
    }
}