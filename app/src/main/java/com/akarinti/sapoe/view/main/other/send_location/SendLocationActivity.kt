package com.akarinti.sapoe.view.main.other.send_location

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.event.LocationUpdateEvent
import com.akarinti.sapoe.event.RouteFethUpdateEvent
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.getLat
import com.akarinti.sapoe.extension.getLong
import com.akarinti.sapoe.model.Route
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.akarinti.sapoe.view.main.MainActivity
import com.akarinti.sapoe.view.main.other.send_location.fragment.SearchRouteDialogFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_send_location.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class SendLocationActivity : BaseMvpActivity<SendLocationPresenter>(), SearchRouteDialogFragment.Listener,
    ConfirmDialogFragment.Listener, FinisDialogFragment.Listener, SendLocationContract.View, ErrorDialogFragment.Listener {

    @Inject
    override lateinit var presenter: SendLocationPresenter

    var mData: LocationListResponse.LocationListData? = null
    var selectedRoute: Route? = null
    private var locationPopup: AlertDialog? = null

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun setup() {
        initView()
        fetchLocationList()
        initAction()
        getCurrentLocation()
    }

    override fun getLayout(): Int = R.layout.activity_send_location

    private fun initView() {
        tvTitle.text = getString(R.string.kirim_lokasi)
        btnAction.text = getString(R.string.kirim)
        checkButton()
        locationPopup = AlertDialog.Builder(this).setMessage(getString(R.string.permission_request))
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()}
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    private fun initAction() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvLocation.setOnClickListener {
            SearchRouteDialogFragment.newInstance()
                .show(supportFragmentManager, SearchRouteDialogFragment::class.java.canonicalName)
        }
        btnAction.setOnClickListener {
            if (!isMockAppInstalled()) {
                ConfirmDialogFragment.newInstance(
                    "", getString(R.string.kirim_lokasi_confirm)
                    , getString(R.string.kirim), getString(R.string.batal)
                ).show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
            }
        }
    }

    private fun checkButton() {
        btnAction.isEnabled = tvLocation.text != getString(R.string.pilih_rute_hint)
    }

    private fun fetchLocationList() {
        showLoading()
        presenter.getLocationList(queryInput = "", page = 1)
    }

    private fun getCurrentLocation() {
        if (checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            myLoc()
        } else {
            requestRequiredPermission()
        }
    }

    override fun selectedRoute(selected: Route) {
        selectedRoute = selected
        tvLocation.text = selected.name
        checkButton()
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
        selectedRoute?.id?.let { id ->
            if (locArr[LocationUtils.LATITUDE_INDEX] != 0.0 && locArr[LocationUtils.LONGTITUDE_INDEX] != 0.0) {
                showLoading()
                presenter.sendLocation(id, locArr[LocationUtils.LATITUDE_INDEX], locArr[LocationUtils.LONGTITUDE_INDEX])
            } else if (getLat() != 0.0 && getLong() != 0.0){
                showLoading()
                presenter.sendLocation(id, getLat(), getLong())
            } else {
                ErrorDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.location_error), getString(R.string.ok_mengerti))
                    .show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
            }
        }
    }

    override fun onConfirmDialogDismissed() {
    }

    override fun onFinishDialogBtnPressed() {
        startActivity(intentFor<MainActivity>())
        finishAffinity()
    }

    override fun getRoutePager(query: String, page: Int) {
        presenter.getLocationList(queryInput = query, page = page)
    }

    override fun getResponse(): LocationListResponse.LocationListData? = mData

    override fun onLocationSet(data: LocationListResponse.LocationListData) {
        dismissLoading()
        mData = data
        RxBus.publish(RouteFethUpdateEvent())
    }

    override fun onLocationSend() {
        dismissLoading()
        FinisDialogFragment.newInstance(
            getString(R.string.kirim_lokasi_finish),
            getString(R.string.kembali_beranda),R.drawable.popup_success
        ).show(supportFragmentManager, FinisDialogFragment::class.java.canonicalName)
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(LocationUpdateEvent::class.java).subscribe {
            locArr = it.location
        })
    }

    override fun onErrorDialogBtnPressed(tag: String?) {
    }

    override fun onErrorDialogDismissed(tag: String?) {
        if (locationPopup?.isShowing == false){
            locationPopup?.show()
        }
    }
}