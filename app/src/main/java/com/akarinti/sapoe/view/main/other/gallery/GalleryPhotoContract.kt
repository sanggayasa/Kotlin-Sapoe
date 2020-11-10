package com.akarinti.sapoe.view.main.other.gallery

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.dummy.GallerySection

interface GalleryPhotoContract {
    interface View : ErrorView {
        fun showLoadingState()
        fun dismissLoadingState()
    }
    interface Presenter {
        fun getPhotoList(isAscending: Boolean): List<GallerySection>
    }
}