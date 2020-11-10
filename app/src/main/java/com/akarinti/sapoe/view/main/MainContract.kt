package com.akarinti.sapoe.view.main

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.model.Unschedule

interface MainContract {
    interface View : ErrorView {
        fun onSentSchedule(dataList: List<Schedule>)
        fun onSentScheduleManual(dataList: List<Unschedule>)
        fun onSentScheduleAuto(dataList: List<Unschedule>)
    }
    interface Presenter {
        fun sendLocationTracking(lat: Double, long: Double, deviceName: String, imei: String)
        fun saveAnswer(input: SaveAnswerBody, lat: Double, long: Double)
        fun saveAnswerUnsch(input: SaveAnswerBody, lat: Double, long: Double)
        fun saveAnswerUnschAuto(input: SaveAnswerBody, lat: Double, long: Double)
    }
}