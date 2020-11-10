package com.akarinti.sapoe.view.main.other.gallery

import android.text.format.DateUtils
import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.model.dummy.GallerySection
import com.akarinti.sapoe.model.dummy.Photo
import com.akarinti.sapoe.model.questionnaire.QuestionOffline
import javax.inject.Inject

class GalleryPhotoPresenter @Inject constructor(
    var inprogressRepository: InprogressRepository
    ): BasePresenter<GalleryPhotoContract.View>(), GalleryPhotoContract.Presenter {

    var result: ArrayList<GallerySection> = ArrayList()
    var today: ArrayList<Photo> = ArrayList()
    var yesterday: ArrayList<Photo> = ArrayList()
    var beforeyesterday: ArrayList<Photo> = ArrayList()

    override fun getPhotoList(isAscending: Boolean): List<GallerySection> {
        view?.showLoadingState()

        result.clear()
        today.clear()
        yesterday.clear()
        beforeyesterday.clear()

        // scheduled - AC
        for(data in inprogressRepository.scheduledAc?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "0", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "0", item.id?:"")
            }
        }

        // scheduled - NB
        for(data in inprogressRepository.scheduledNb?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "0", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "0", item.id?:"")
            }
        }

        // scheduled - CL
        for(data in inprogressRepository.scheduledCl?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "0", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "0", item.id?:"")
            }
        }

        // unscheduled - AC
        for(data in inprogressRepository.unscheduledAc?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", item.id?:"")
            }
        }

        // unscheduled - NB
        for(data in inprogressRepository.unscheduledNb?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", item.id?:"")
            }
        }

        // unscheduled - CL
        for(data in inprogressRepository.unscheduledCl?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", item.id?:"")
            }
        }

        // unscheduled - MCDS
        for(data in inprogressRepository.unscheduledMcds?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", item.id?:"")
            }
        }

        // unscheduled - Qc
        for(data in inprogressRepository.unscheduledQc?: ArrayList()) {
            checkQuestion(data.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", "master")
            for (item in data.itemList?: ArrayList()) {
                checkQuestion(item.formList, data.start, data.status, data.agentName, data.location?.name, data.id, "1", item.id?:"")
            }
        }

        if (isAscending) {
            if (beforeyesterday.size > 0) {
                result.add(GallerySection(true, "2 hari yang lalu"))
                beforeyesterday.sortBy { it.dateTime }
                for (item in beforeyesterday) {
                    result.add(GallerySection(item))
                }
            }

            if (yesterday.size > 0) {
                result.add(GallerySection(true, "Kemarin"))
                yesterday.sortBy { it.dateTime }
                for (item in yesterday) {
                    result.add(GallerySection(item))
                }
            }

            if (today.size > 0) {
                result.add(GallerySection(true, "Hari ini"))
                today.sortBy { it.dateTime }
                for (item in today) {
                    result.add(GallerySection(item))
                }
            }
        } else {
            if (today.size > 0) {
                result.add(GallerySection(true, "Hari ini"))
                today.sortByDescending { it.dateTime }
                for (item in today) {
                    result.add(GallerySection(item))
                }
            }

            if (yesterday.size > 0) {
                result.add(GallerySection(true, "Kemarin"))
                yesterday.sortByDescending { it.dateTime }
                for (item in yesterday) {
                    result.add(GallerySection(item))
                }
            }

            if (beforeyesterday.size > 0) {
                result.add(GallerySection(true, "2 hari yang lalu"))
                beforeyesterday.sortByDescending { it.dateTime }
                for (item in beforeyesterday) {
                    result.add(GallerySection(item))
                }
            }
        }
        view?.dismissLoadingState()
        return result
    }

    private fun checkQuestion(dataList: List<QuestionOffline>?, start: Long?, status: String?, agentName: String?, locationName: String?, id: String?, type: String, parent: String) {
        for (formlist in dataList?: ArrayList()) {
            for (question in formlist.questionList?: ArrayList()) {
                if (question.answerType == "picture" && null != question.answerLocal) {
                    if (null != start) {
                        val photo = Photo(
                            status,
                            start?:0L,
                            agentName,
                            locationName,
                            "${type}_${id}_${question.id}_${parent}",
                            question.answerLocal
                        )
                        if (DateUtils.isToday(start*1000L))
                            today.add(photo)
                        else if(DateUtils.isToday(start*1000L + DateUtils.DAY_IN_MILLIS))
                            yesterday.add(photo)
                        else if(DateUtils.isToday(start*1000L + (2 * DateUtils.DAY_IN_MILLIS)))
                            beforeyesterday.add(photo)
                    }
                }
            }
        }
    }
}