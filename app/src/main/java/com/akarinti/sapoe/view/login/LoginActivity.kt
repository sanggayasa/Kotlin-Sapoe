package com.akarinti.sapoe.view.login

import android.Manifest
import android.text.method.SingleLineTransformationMethod
import android.view.KeyEvent
import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.event.PermissionGrantedEvent
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.isFilled
import com.akarinti.sapoe.extension.isMinimum
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.component.AsteriskPasswordTransformationMethod
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.main.MainActivity
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginContract.View,
    ErrorDialogFragment.Listener {

    @Inject
    override lateinit var presenter: LoginPresenter

    private var deviceImei: String? = null

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        presenter.checkVersion()
        initView()
        initAction()
        getDeviceImei()
    }
    override fun getLayout(): Int = R.layout.activity_login

    private fun initView() {
        etPassword.transformationMethod = AsteriskPasswordTransformationMethod.getInstance()
    }

    private fun initAction() {
        ivTogglePassword.setOnClickListener {
            val start = etPassword.selectionStart
            val end = etPassword.selectionEnd
            if (etPassword.transformationMethod is AsteriskPasswordTransformationMethod) {
                etPassword.transformationMethod = SingleLineTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.ic_show_password_active)
            } else {
                etPassword.transformationMethod = AsteriskPasswordTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.ic_show_password_inactive)
            }
            etPassword.setSelection(start, end)
        }
        btnLogin.setOnClickListener { login() }
        etPassword.setOnEditorActionListener { _, _, event ->
            if (null == event) {
                if(btnLogin.isEnabled) login()
                return@setOnEditorActionListener true
            }
            false
        }
        etPassword.onKeyUpListener = {
            if(it == KeyEvent.KEYCODE_ENTER
                || it == KeyEvent.KEYCODE_NUMPAD_ENTER){
                if(btnLogin.isEnabled) login()
            }
        }
    }

    private fun getDeviceImei() {
        if (checkPermissions(Manifest.permission.READ_PHONE_STATE)) {
            deviceImei = DeviceInfo.getIMEI(this)
        } else {
            requestRequiredPermission()
        }
    }

    private fun checkLogin() {
        btnLogin.isEnabled = etPassword.isMinimum(8) && etUserid.isFilled()
    }

    private fun login() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
            //todo: default to 123 using android 10
            showLoading()
            presenter.loginUser(etUserid.text.toString(), etPassword.text.toString(), "123")
        } else {
            if (null == deviceImei || deviceImei == "null") {
                getDeviceImei()
            } else {
                showLoading()
                presenter.loginUser(etUserid.text.toString(), etPassword.text.toString(), deviceImei!!)
            }
        }
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(etUserid.textChanges().observeOn(AndroidSchedulers.mainThread()).subscribe {checkLogin()})
        addUiSubscription(etPassword
            .textChanges().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              //  if (it.isNotEmpty()) {
                    ivTogglePassword.visibility = View.VISIBLE
                //} else {
              //      ivTogglePassword.visibility = View.INVISIBLE
              //  }
                checkLogin()
            })
        addUiSubscription(RxBus.listen(PermissionGrantedEvent::class.java).subscribe {
            deviceImei = DeviceInfo.getIMEI(this)
            if (btnLogin.isEnabled)
                login()
        })
    }

    override fun onErrorDialogBtnPressed(tag: String?) {
    }

    override fun onErrorDialogDismissed(tag: String?) {
    }

    override fun gotoNextPage() {
        dismissLoading()
        startActivity(intentFor<MainActivity>())
        finishAffinity()
    }

    override fun errorScreen(message: String?, code: Int?) {
        showErrorPopup(message)
    }

    override fun errorScreen(message: String?) {
        showErrorPopup(message)
    }

    private fun showErrorPopup(message: String?) {
        dismissLoading()
        ErrorDialogFragment.newInstance(
            Params.TAG_LOGIN, (if (isNetworkAvailable()) {
                message?:getString(R.string.userid_tidak_terdaftar)
            }
            else
            {
                getString(R.string.network_offline)
            })
            , getString(R.string.tutup)
        ).show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
    }

    override fun onResume() {
        super.onResume()
        //isMockAppInstalled()
    }
}