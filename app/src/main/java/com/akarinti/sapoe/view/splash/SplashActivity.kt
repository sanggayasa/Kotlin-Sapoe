package com.akarinti.sapoe.view.splash

import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.view.login.LoginActivity
import com.akarinti.sapoe.view.main.MainActivity
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.intentFor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : BaseMvpActivity<SplashPresenter>(), SplashContract.View {

    @Inject
    override lateinit var presenter: SplashPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initSplash()
    }

    override fun getLayout(): Int = R.layout.activity_splash

    fun initSplash() =  addUiSubscription(
        Observable.timer(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { presenter.getToken() }
    )

    override fun onNextScreen(isLoggedIn: Boolean) {
        if (isLoggedIn)
            startActivity(intentFor<MainActivity>())
        else
            startActivity(intentFor<LoginActivity>())

        finishAffinity()
    }
}