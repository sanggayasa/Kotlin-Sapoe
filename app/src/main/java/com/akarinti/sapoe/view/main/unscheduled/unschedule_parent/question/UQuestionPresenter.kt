package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.question

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.repository.InprogressRepository
import javax.inject.Inject

class UQuestionPresenter @Inject constructor(
    val inprogressRepository: InprogressRepository
) : BasePresenter<UQuestionContract.View>(), UQuestionContract.Presenter