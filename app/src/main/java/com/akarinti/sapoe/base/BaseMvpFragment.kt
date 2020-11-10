package com.akarinti.sapoe.base

import com.akarinti.sapoe.data.repository.ProfileRepository

abstract class BaseMvpFragment<T: BasePresenter<*>>: BaseFragment() {
    protected abstract var presenter: T

    override fun internalSetup() {
        initPresenterView()
        super.internalSetup()
    }

    abstract fun initPresenterView()

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun forceLogout() {
        presenter.forceLogout()
        super.forceLogout()
    }
    fun userData(): ProfileRepository = presenter.headerManager.profileRepository

}