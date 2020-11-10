package com.akarinti.sapoe.view.main.visit.visit_parent.question

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.repository.InprogressRepository
import javax.inject.Inject

class SQuestionPresenter @Inject constructor(
    val inprogressRepository: InprogressRepository
) : BasePresenter<SQuestionContract.View>(), SQuestionContract.Presenter