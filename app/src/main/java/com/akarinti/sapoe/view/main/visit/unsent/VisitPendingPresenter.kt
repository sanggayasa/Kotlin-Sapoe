package com.akarinti.sapoe.view.main.visit.unsent

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.model.Schedule
import javax.inject.Inject

class VisitPendingPresenter @Inject constructor(
    private var archiveRepository: ArchiveRepository,
    private var profileRepository: ProfileRepository
) : BasePresenter<VisitPendingContract.View>(), VisitPendingContract.Presenter {

    override fun unsentList(): ArrayList<Schedule> {
        return ArrayList((archiveRepository.unsentSchedule?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }
}