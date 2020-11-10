package com.akarinti.sapoe.base

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Location
import android.location.LocationListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akarinti.sapoe.R
import com.akarinti.sapoe.event.LanguageChangedEvent
import com.akarinti.sapoe.event.LocationUpdateEvent
import com.akarinti.sapoe.event.PermissionGrantedEvent
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.locale.LocaleUtils
import com.akarinti.sapoe.model.Version
import com.akarinti.sapoe.model.ui.AnimationSet
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.utils.MockLocationDetector
import com.akarinti.sapoe.view.component.dialog.LoadingProgressDialog
import com.akarinti.sapoe.view.component.dialog.WarningPopup
import com.akarinti.sapoe.view.login.LoginActivity
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast
import java.net.HttpURLConnection

abstract class BaseActivity:AppCompatActivity(), ErrorView, /*UpdateDialogFragment.Listener,*/ LocationListener {

    private val uiCompositeDisposable:CompositeDisposable = CompositeDisposable()
    private var backPressed = false
    open var doubleBackExit = false
    open var locArr:DoubleArray = DoubleArray(2)
    open var overrideAnimation: AnimationSet? = null
        set(value) {
            if (null != value) {
                overridePendingTransition(value.animEnterIn, value.animEnterOut)
                field = value
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectView()
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initLayout()
        initSubscription()
        internalSetup()
        initAnimation()
        setup()
    }

    override fun attachBaseContext(newBase: Context?) {
        //multilanguage
        super.attachBaseContext(LocaleUtils.onAttach(newBase, false))
    }

    open fun internalSetup(){}

    abstract fun injectView()

    abstract fun setup()

    abstract fun getLayout():Int

    fun initAnimation(){
        overrideAnimation = AnimationSet(
            R.anim.fade_in, R.anim.stay_in_place,
            R.anim.stay_in_place, R.anim.fade_out)
    }

    fun initLayout(){
        if(getLayout() != 0)
            setContentView(getLayout())
    }

    open fun initSubscription() {
        addUiSubscription(RxBus.listen(LanguageChangedEvent::class.java).subscribe {
            recreate()
        })
    }

    //addUiSubscription(RxBus.listen(ClassName::class.java).subscribe{})
    fun addUiSubscription(disposable: Disposable){
        uiCompositeDisposable.add(disposable)
    }

    fun clearAllSubscription(){
        uiCompositeDisposable.clear()
    }

    fun isLoading(): Boolean {
        return null != supportFragmentManager
            .findFragmentByTag(LoadingProgressDialog::class.java.canonicalName) as LoadingProgressDialog?
    }

    fun showLoading() {
        Handler().post {
            if (!isDestroyed) {
                supportFragmentManager.executePendingTransactions()
                var progressDialog = supportFragmentManager
                    .findFragmentByTag(LoadingProgressDialog::class.java.canonicalName) as LoadingProgressDialog?

                if (progressDialog == null) {
                    progressDialog = LoadingProgressDialog.newInstance()
                    progressDialog.show(supportFragmentManager, LoadingProgressDialog::class.java.canonicalName)
                }
            }
        }
    }

    fun dismissLoading() {
        Handler().post {
            if (!isDestroyed) {
                supportFragmentManager.executePendingTransactions()
                val progressDialog = supportFragmentManager
                    .findFragmentByTag(LoadingProgressDialog::class.java.canonicalName) as LoadingProgressDialog?
                progressDialog?.dismiss()
            }
        }
    }

    override fun errorScreen(message: String?, code: Int?) {
        dismissLoading()
        if (isNetworkAvailable()) {
            if (code == HttpURLConnection.HTTP_UNAUTHORIZED || code == HttpURLConnection.HTTP_FORBIDDEN) {
                message?.let { toast(it) }
                forceLogout()
            } else {
                message?.let { toast(it) }
            }
        } else {
            toast(R.string.network_offline)
        }
    }

    override fun errorScreen(message: String?) {
        dismissLoading()
        if (isNetworkAvailable())
            message?.let { toast(it) }
        else
            toast(R.string.network_offline)
    }

    override fun errorConnection() {
        toast(R.string.network_offline)
    }

    override fun forceLogout() {
        startActivity(intentFor<LoginActivity>().clearTop().newTask())
        finishAffinity()
    }

    override fun onDestroy() {
        clearAllSubscription()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        if(null != overrideAnimation) {
            overridePendingTransition(overrideAnimation!!.animExitIn, overrideAnimation!!.animExitOut)
        }
    }

    override fun onBackPressed() {
        if (doubleBackExit) {
            if (backPressed) {
                finishAffinity()
            } else {
                backPressed = true
                Toast.makeText(this, getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed({ backPressed = false }, 2000)
            }
        } else
            super.onBackPressed()
    }

    fun requestRequiredPermission() {
        MayI.withActivity(this)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onResult(this::permissionResultMulti)
            .onRationale(this::permissionRationaleSingle)
            .check()
    }

    private fun permissionResultMulti(permission: Array<PermissionBean>) {
        var granted = true
        var permanentDenied = false
        run loop@ {
            permission.forEach {
                if (it.isPermanentlyDenied) {
                    granted = false
                    permanentDenied = true
                    return@loop
                } else if (!it.isGranted) {
                    granted = false
                }
            }
        }

        if (permanentDenied) {
            AlertDialog.Builder(this).setMessage(getString(R.string.permission_request))
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    goToSettings()
                    dialog.dismiss()}
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create().show()
        } else if (granted) {
            val tmpArr = myLoc()
            locArr[LocationUtils.LATITUDE_INDEX] = tmpArr[LocationUtils.LATITUDE_INDEX]
            locArr[LocationUtils.LONGTITUDE_INDEX] = tmpArr[LocationUtils.LONGTITUDE_INDEX]
            RxBus.publish(LocationUpdateEvent(locArr))
            RxBus.publish(PermissionGrantedEvent())
        }
    }

    private fun permissionRationaleSingle(bean: Array<PermissionBean>, token: PermissionToken) {
        token.continuePermissionRequest()
    }

    private fun goToSettings() {
        val myAppSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        myAppSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        myAppSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivityForResult(myAppSettings, CodeIntent.APP_SETTINGS)
    }

    fun myLoc():DoubleArray{
        return LocationUtils.getMyLocation(this,object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                locArr[LocationUtils.LATITUDE_INDEX] = p0?.latitude!!.let { it }
                locArr[LocationUtils.LONGTITUDE_INDEX] = p0.longitude.let { it }
                RxBus.publish(LocationUpdateEvent(locArr))
            }
            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
            override fun onProviderEnabled(p0: String?) {}
            override fun onProviderDisabled(p0: String?) {}
        })
    }

    override fun onVersionCheck(data: Version?) {}

    override fun onLocationChanged(p0: Location?) {
        if (MockLocationDetector.isLocationFromMockProvider(this, p0!!)){
            showErrorFakeGPS()
            //errorScreen(getString(R.string.fake_gps_installed))
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    fun showErrorFakeGPS() {
        WarningPopup.newInstance(getString(R.string.fake_gps_installed)).show(supportFragmentManager, WarningPopup::class.java.canonicalName)
        WarningPopup.listener = object : WarningPopup.Listener{
            override fun onActionButton() {
                finishAffinity()
            }
        }
    }


}