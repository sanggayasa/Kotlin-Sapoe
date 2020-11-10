package com.akarinti.sapoe.base

abstract class BaseMvpDialogFragment<T: BasePresenter<*>>: BaseDialogFragment() {
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
}