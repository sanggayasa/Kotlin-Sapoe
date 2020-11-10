package com.akarinti.sapoe.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akarinti.sapoe.model.Version
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : androidx.fragment.app.Fragment(), ErrorView{

    private val compositeDisposable:CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if(getLayout() != 0)
            inflater.inflate(getLayout(),container,false)
        else
            null
    }

    fun addUiSubscription(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    fun clearAllSubscription(){
        compositeDisposable.clear()
    }

    fun getBaseActivity(): BaseActivity? =
        if (activity is BaseActivity) {
            activity as BaseActivity
        } else null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injectView()
        initInjection()
        super.onViewCreated(view, savedInstanceState)
        initSubscription()
        internalSetup()
        setup()
    }

    open fun internalSetup(){}

    open fun initInjection(){}

    abstract fun injectView()

    //addUiSubscription(RxBus.listen(ClassName::class.java).subscribe{})
    open fun initSubscription() {}

    override fun onDestroyView() {
        super.onDestroyView()
        clearAllSubscription()
    }

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

    abstract fun setup()
    abstract fun getLayout():Int

}