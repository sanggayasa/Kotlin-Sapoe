package com.akarinti.sapoe.view.main.unscheduled.unsent

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.repository.ArchiveRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.model.Unschedule
import javax.inject.Inject

class UnscheduledPendingPresenter @Inject constructor(
    private val archiveRepository: ArchiveRepository,
    private val profileRepository: ProfileRepository
) : BasePresenter<UnscheduledPendingContract.View>(), UnscheduledPendingContract.Presenter {
    override fun manualList(): ArrayList<Unschedule> {
        return ArrayList((archiveRepository.unsentUnSchedule?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }

    override fun autoList(): ArrayList<Unschedule> {
        return ArrayList((archiveRepository.unsentUnScheduleAuto?: listOf()).filter { it.agent?.id == profileRepository.profile?.id })
    }
}