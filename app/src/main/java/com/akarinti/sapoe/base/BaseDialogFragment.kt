package com.akarinti.sapoe.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.akarinti.sapoe.model.Version
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseDialogFragment : androidx.fragment.app.DialogFragment(), ErrorView {

    private val compositeDisposable:CompositeDisposable = CompositeDisposable()

    fun addUiSubscription(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    fun clearAllSubscription(){
        compositeDisposable.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupDialogStyle(dialog)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (0 != getLayout()) {
            inflater.inflate(getLayout(), container, false)
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInjection()
        super.onViewCreated(view, savedInstanceState)
        initSubscription()
        internalSetup()
        setup()
    }

    abstract fun setupDialogStyle(dialog: Dialog)

    abstract fun loadArguments()

    open fun initInjection(){}

    open fun internalSetup(){}

    //addUiSubscription(RxBus.listen(ClassName::class.java).subscribe{})
    open fun initSubscription() {}

    override fun onDestroyView() {
        super.onDestroyView()
        clearAllSubscription()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (ignored: IllegalStateException) { }
    }

    override fun dismiss() {
        if (isStateSaved) {
            dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    abstract fun setup()
    abstract fun getLayout():Int

    fun isLoading(): Boolean {
        return (activity as? BaseActivity)?.isLoading()?:false
    }

    fun showLoading() {
        (activity as? BaseActivity)?.showLoading()
    }

    fun dismissLoading() {
        (activity as? BaseActivity)?.dismissLoading()
    }

    override fun errorScreen(message: String?, code: Int?) {
        (activity as? BaseActivity)?.errorScreen(message, code)
    }

    override fun errorScreen(message: String?) {
        (activity as? BaseActivity)?.errorScreen(message)
    }

    override fun errorConnection() {
        (activity as? BaseActivity)?.errorConnection()
    }

    override fun forceLogout() {
        (activity as? BaseActivity)?.forceLogout()
    }

    override fun onVersionCheck(data: Version?) {
        (activity as? BaseActivity)?.onVersionCheck(data)
    }
}
