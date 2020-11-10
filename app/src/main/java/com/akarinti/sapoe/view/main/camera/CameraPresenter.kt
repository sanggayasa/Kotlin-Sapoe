package com.akarinti.sapoe.view.main.camera

import com.akarinti.sapoe.base.BasePresenter
import javax.inject.Inject


class CameraPresenter @Inject constructor(
) : BasePresenter<CameraContract.View>(),
    CameraContract.Presenter {
    override fun loadFoto() {
    }

}