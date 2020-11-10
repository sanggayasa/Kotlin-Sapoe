package com.akarinti.sapoe.view.main.unscheduled.rute

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.event.RouteFethUpdateEvent
import com.akarinti.sapoe.event.UpdateStatusUnscheduleEvent
import com.akarinti.sapoe.extension.getLat
import com.akarinti.sapoe.extension.getLong
import com.akarinti.sapoe.model.Route
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.other.send_location.fragment.SearchRouteDialogFragment
import com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.UnscheduleParentActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_rute.*
import kotlinx.android.synthetic.main.button_navigation.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import javax.inject.Inject

class RuteActivity : BaseMvpActivity<RutePresenter>(), RuteContract.View, SearchRouteDialogFragment.Listener,
    ConfirmDialogFragment.Listener, ErrorDialogFragment.Listener  {

    var selectedRoute: Route? = null
    var locationId=""
    var createdUnschedule: Unschedule? = null
    var mData: LocationListResponse.LocationListData? = null

    override fun onConfirmDialogBtnPressed(tag: String?) {
        when (tag) {
            Params.TAG_EXIT -> {
                finish()
            }
            Params.TAG_CREATE -> {
                showLoading()
                val type = when(spType.selectedItem) {
                    "Clean" -> Params.TYPE_CLEAN
                    "Neon Box" -> Params.TYPE_NEONBOX
                    "AC" -> Params.TYPE_AC
                    "MCDS" -> Params.TYPE_MCDS
                    "Survey" -> Params.TYPE_QC
                    else -> ""
                }
                presenter.createUnschedule(locationId, type, getLat(), getLong())
            }
            Params.TAG_TAKE -> {
                createdUnschedule?.let {
                    startActivity(UnscheduleParentActivity.newInstance(this, it))
                }
                finish()
            }
        }
    }

    override fun onConfirmDialogDismissed() {
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
        when (tag) {
            Params.TAG_TAKE -> {
                finish()
            }
        }
    }

    @Inject
    override lateinit var presenter: RutePresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initView()
        initAction()
        checkButton()
        initSpinner()
        fetchLocationList()
    }
    override fun onLocationSet(data: LocationListResponse.LocationListData) {
        dismissLoading()
        mData = data
        RxBus.publish(RouteFethUpdateEvent())
    }

    override fun selectedRoute(selected: Route) {
        if (null != selected.lat && null != selected.long) {
            selectedRoute = selected
            locationId = selected.id.toString()
            tvLocation.text = selected.name
        } else {
            tvLocation.setText(R.string.pilih_rute_hint)
            ErrorDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.need_location_request), getString(R.string.ok_mengerti))
                .show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)

        }
        checkButton()
    }

    override fun getRoutePager(query: String, page: Int) {
        presenter.getLocationList(queryInput = query, page = page)
    }

    override fun getResponse(): LocationListResponse.LocationListData? = mData

    override fun getLayout(): Int = R.layout.activity_rute

    private fun initView() {
        val type = arrayOf("-","Clean", "Neon Box", "AC", "MCDS", "Survey")
        val adapterType= ArrayAdapter<String>(this,
            R.layout.item_spinner,type)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spType.adapter = adapterType

        tvTitle.text = getString(R.string.buat_rute)
    }

    private fun initAction() {
        ivBack.setOnClickListener {
            if(spType.selectedItem!=getString(R.string.strip) && tvLocation.text != getString(R.string.pilih_rute_hint))
                showKeluarPopup()
            else {
                onBackPressed()
            }
        }
        tvLocation.setOnClickListener {
            SearchRouteDialogFragment.newInstance()
                .show(supportFragmentManager, SearchRouteDialogFragment::class.java.canonicalName)
        }
        btnSimpan.setOnClickListener {
            showConfirmPopup()
        }
    }

    override fun onBackPressed() {
        if(spType.selectedItem!=getString(R.string.strip) && tvLocation.text != getString(R.string.pilih_rute_hint))
            showKeluarPopup()
        else
            super.onBackPressed()
    }

    override fun successCreateRoute(data: Unschedule?) {
        RxBus.publish(UpdateStatusUnscheduleEvent())
        dismissLoading()
        createdUnschedule = data
        showKerjakanPopup()
    }

    private fun initSpinner(){
        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkButton()
            }
        }
    }

    private fun fetchLocationList() {
        showLoading()
        presenter.getLocationList("")
    }

    private fun showConfirmPopup() {
        ConfirmDialogFragment.newInstance(
            Params.TAG_CREATE, getString(R.string.rute_takterduga)
            , getString(R.string.buat_rute), getString(R.string.batal)
        ).show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
    }
    private fun showKerjakanPopup() {
        ConfirmDialogFragment.newInstance(
            Params.TAG_TAKE, getString(R.string.rute_takterduga_sukses)
            , getString(R.string.ya), getString(R.string.nanti),R.drawable.popup_success
        ).show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
    }

    private fun showKeluarPopup() {
        ConfirmDialogFragment.newInstance(
            Params.TAG_EXIT, getString(R.string.yakin_batal_rute)
            , getString(R.string.keluar), getString(R.string.simpan)
        ).show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
    }
    private fun checkButton() {
        btnSimpan.isEnabled = (spType.selectedItem!=getString(R.string.strip) && tvLocation.text != getString(R.string.pilih_rute_hint))
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
        if (tag == Params.TAG_TAKE)
            finish()
    }

    override fun onErrorDialogDismissed(tag: String?) {
    }
}