package com.akarinti.sapoe.view.main.ticket.add.foto

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.repository.ImageRepository
import com.akarinti.sapoe.model.answer.ImageResult
import javax.inject.Inject

class FotoBeforePresenter @Inject constructor(
    val imageRepository: ImageRepository
) : BasePresenter<FotoBeforeContract.View>(), FotoBeforeContract.Presenter {
    override fun getRepository(): List<ImageResult> = imageRepository.imageResult ?: ArrayList()

}