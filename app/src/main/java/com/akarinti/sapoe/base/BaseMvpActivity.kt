package com.akarinti.sapoe.base

import com.akarinti.sapoe.SapoeApp
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.model.Version
import com.akarinti.sapoe.utils.DownloadUtils
import com.akarinti.sapoe.utils.MockLocationDetector
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.akarinti.sapoe.view.component.dialog.UpdateDialogFragment
import com.akarinti.sapoe.view.splash.SplashActivity

abstract class BaseMvpActivity<T: BasePresenter<*>>: BaseActivity(), UpdateDialogFragment.Listener {
    protected abstract var presenter: T

    override fun internalSetup() {
        initPresenterView()
        super.internalSetup()
    }

    abstract fun initPresenterView()

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun forceLogout() {
        presenter.forceLogout()
        super.forceLogout()
    }

    fun userData(): ProfileRepository = presenter.headerManager.profileRepository

    private fun checkVersion() {
        supportFragmentManager.executePendingTransactions()
        if ((application as SapoeApp).isNeedValidate() && this !is SplashActivity &&
            (null == supportFragmentManager.findFragmentByTag(FinisDialogFragment::class.java.canonicalName) || null == supportFragmentManager.findFragmentByTag(ConfirmDialogFragment::class.java.canonicalName))) {

        }
    }

    fun isMockAppInstalled(): Boolean {
        if (MockLocationDetector.checkForAllowMockLocationsApps(this, presenter.headerManager.appList)) {
            showErrorFakeGPS()
            return true
        }
        return false
    }

    override fun onVersionCheck(data: Version?) {
        data?.let {
            if (it.updateApp == true) {
                UpdateDialogFragment.newInstance(it.forceUpdate == true, it.url)
                    .show(supportFragmentManager, UpdateDialogFragment::class.java.canonicalName)
            } else {
                isMockAppInstalled()
            }
        }
    }

    override fun onUpdate(url: String?) {
        showLoading()
        url?.let {
            DownloadUtils(this, object : DownloadUtils.Listener {
                override fun dismissDownloadLoading() { dismissLoading() }
                override fun errorDownload(msg: String) { errorScreen(msg) }
            }).downloadAPK(it)
        }
    }

    override fun onCancel() { isMockAppInstalled() }
}