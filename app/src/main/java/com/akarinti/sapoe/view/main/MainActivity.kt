package com.akarinti.sapoe.view.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseFragment
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.event.*
import com.akarinti.sapoe.extension.*
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.objects.TabList
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.main.home.HomeFragment
import com.akarinti.sapoe.view.main.other.OtherFragment
import com.akarinti.sapoe.view.main.ticket.TicketFragment
import com.akarinti.sapoe.view.main.unscheduled.UnscheduledFragment
import com.akarinti.sapoe.view.main.visit.VisitFragment
import com.jaredrummler.android.device.DeviceName
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueRootTabHistoryStrategy
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.Observable
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : BaseMvpActivity<MainPresenter>(), MainContract.View, HasAndroidInjector, HomeFragment.Listener {

    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var presenter: MainPresenter

    private lateinit var fragNavController: FragNavController

    private var locationPopup: AlertDialog? = null

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun setup() {
        presenter.checkVersion()
        initTab()
        initAction()
        initView()
        doubleBackExit = true
        gatherPayloadLocationTracking()
        sendLocationTracking()
        proccessUnsent()
        proccessUnsentManual()
        proccessUnsentAuto()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)
    }

    override fun getLayout(): Int = R.layout.activity_main
    private fun initView(){
        locationPopup = AlertDialog.Builder(this).setMessage(getString(R.string.permission_request))
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()}
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
    }
    private fun initTab() {
        ViewCompat.setOnApplyWindowInsetsListener(frameMain,
            object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(
                    v: View,
                    insets: WindowInsetsCompat?
                ): WindowInsetsCompat {
                    var insets = insets
                    insets = ViewCompat.onApplyWindowInsets(v, insets)
                    if (insets.isConsumed) {
                        return insets
                    }

                    var consumed = false
                    var i = 0
                    val count = frameMain.childCount
                    while (i < count) {
                        ViewCompat.dispatchApplyWindowInsets(frameMain.getChildAt(i), insets)
                        if (insets.isConsumed) {
                            consumed = true
                        }
                        i++
                    }
                    return if (consumed) insets.consumeSystemWindowInsets() else insets
                }
            })
        fragNavController = FragNavController(supportFragmentManager, R.id.frameMain)
        fragNavController.navigationStrategy =
            UniqueRootTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    fragNavController.switchTab(index, transactionOptions)
                }
            })
        fragNavController.fragmentHideStrategy = FragNavController.HIDE
        fragNavController.createEager = true
        val fragments = java.util.ArrayList<BaseFragment>()
        fragments.add(HomeFragment())
        fragments.add(VisitFragment())
        fragments.add(UnscheduledFragment())
        fragments.add(TicketFragment())
        fragments.add(OtherFragment())
        fragNavController.rootFragments = fragments
        refreshTab()
    }

    fun selectTab(tabIndex: Int) {
        if (tabIndex >= 0 && null != fragNavController.rootFragments
            && tabIndex < fragNavController.rootFragments!!.size) {
            fragNavController.switchTab(tabIndex)
            refreshTab()
        }
    }

    private fun refreshTab() {
        tvHome.alpha = if (fragNavController.currentStackIndex == TabList.Home) 1f else 0.6f
        tvVisit.alpha = if (fragNavController.currentStackIndex == TabList.Visit) 1f else 0.6f
        tvUnscheduled.alpha = if (fragNavController.currentStackIndex == TabList.Unscheduled) 1f else 0.6f
        tvTicket.alpha = if (fragNavController.currentStackIndex == TabList.Ticket) 1f else 0.6f
        tvOther.alpha = if (fragNavController.currentStackIndex == TabList.Other) 1f else 0.6f
    }

    private fun initAction() {
        tvHome.setOnClickListener { selectTab(TabList.Home) }
        tvVisit.setOnClickListener { selectTab(TabList.Visit) }
        tvUnscheduled.setOnClickListener { selectTab(TabList.Unscheduled) }
        tvTicket.setOnClickListener { selectTab(TabList.Ticket) }
        tvOther.setOnClickListener { selectTab(TabList.Other) }
    }

    private fun gatherPayloadLocationTracking() {
        if (checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermissions(Manifest.permission.READ_PHONE_STATE)) {
            myLoc()
        } else {
            requestRequiredPermission()
        }
    }

    private fun proccessUnsent() {
        presenter.archiveRepository.unsentSchedule?.let {
            if (it.isNotEmpty()) {
                sendSchedule(it[0])
            }
        }
    }
    private fun proccessUnsentManual() {
        presenter.archiveRepository.unsentUnSchedule?.let {
            if (it.isNotEmpty()) {
                sendUncheduleManual(it[0])
            }
        }
    }
    private fun proccessUnsentAuto() {
        presenter.archiveRepository.unsentUnScheduleAuto?.let {
            if (it.isNotEmpty()) {
                sendUncheduleAuto(it[0])
            }
        }
    }

    private fun sendSchedule(schedule: Schedule) {
        when (schedule.type) {
            Params.TYPE_AC -> {
                presenter.inprogressRepository.scheduledAc?.find { it.id == schedule.id!! }?.let { data ->
                    val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                    val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                    for (child in data.itemList ?: listOf()) {
                        for (form in child.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    child.id!!,
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                    }

                    for (form in data.formList ?: listOf()) {
                        for (answer in form.questionList ?: listOf()) {
                            answerList += SaveAnswerBody.Answer(
                                "",
                                answer.id!!,
                                answer.answerLocal?:""
                            )
                        }
                    }
                    durationList += SaveAnswerBody.Duration(
                        "",
                        data.start,
                        data.end
                    )
                    presenter.saveAnswer(
                        SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_SCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                }
            }
            Params.TYPE_NEONBOX -> {
                presenter.inprogressRepository.scheduledNb?.find { it.id == schedule.id!! }?.let { data ->
                    val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                    val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                    for (child in data.itemList ?: listOf()) {
                        for (form in child.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    child.id!!,
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                    }

                    for (form in data.formList ?: listOf()) {
                        for (answer in form.questionList ?: listOf()) {
                            answerList += SaveAnswerBody.Answer(
                                "",
                                answer.id!!,
                                answer.answerLocal?:""
                            )
                        }
                    }
                    durationList += SaveAnswerBody.Duration(
                        "",
                        data.start,
                        data.end
                    )
                    presenter.saveAnswer(SaveAnswerBody(
                        orderId = data.id!!,
                        orderType = data.type!!,
                        category = Params.CATEGORY_SCHEDULED,
                        answerList = answerList,
                        duration = durationList
                    ), getLat(), getLong())
                }
            }
            Params.TYPE_CLEAN -> {
                presenter.inprogressRepository.scheduledCl?.find { it.id == schedule.id!! }?.let { data ->
                    val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                    val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                    for (child in data.itemList ?: listOf()) {
                        for (form in child.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    child.id!!,
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                    }

                    for (form in data.formList ?: listOf()) {
                        for (answer in form.questionList ?: listOf()) {
                            answerList += SaveAnswerBody.Answer(
                                "",
                                answer.id!!,
                                answer.answerLocal?:""
                            )
                        }
                    }
                    durationList += SaveAnswerBody.Duration(
                        "",
                        data.start,
                        data.end
                    )
                    presenter.saveAnswer(SaveAnswerBody(
                        orderId = data.id!!,
                        orderType = data.type!!,
                        category = Params.CATEGORY_SCHEDULED,
                        answerList = answerList,
                        duration = durationList,
                        suhuAnswer = data.suhu_answer?:"",
                        suhuPicture = data.suhu_picture?:""
                    ), getLat(), getLong())
                }
            }
        }
    }
    private fun sendUncheduleManual(schedule: Unschedule) {
            when (schedule.type) {
                Params.TYPE_AC -> {
                    presenter.inprogressRepository.unscheduledAc?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnsch(
                            SaveAnswerBody(
                                orderId = data.id!!,
                                orderType = data.type!!,
                                category = Params.CATEGORY_UNSCHEDULED,
                                answerList = answerList,
                                duration = durationList
                            ), getLat(), getLong())
                    }
                }
                Params.TYPE_NEONBOX -> {
                    presenter.inprogressRepository.unscheduledNb?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnsch(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                    }
                }
                Params.TYPE_CLEAN -> {
                    presenter.inprogressRepository.unscheduledCl?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnsch(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList,
                            suhuAnswer = data.suhu_answer?:"",
                            suhuPicture = data.suhu_picture?:""
                        ), getLat(), getLong())
                    }
                }
                Params.TYPE_MCDS -> {
                    presenter.inprogressRepository.unscheduledMcds?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnsch(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                    }
                }
                Params.TYPE_QC -> {
                    presenter.inprogressRepository.unscheduledQc?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnsch(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                    }
                }
            }
        }
    private fun sendUncheduleAuto(schedule: Unschedule) {
            when (schedule.type) {
                Params.TYPE_AC -> {
                    presenter.inprogressRepository.unscheduledAc?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnschAuto(
                            SaveAnswerBody(
                                orderId = data.id!!,
                                orderType = data.type!!,
                                category = Params.CATEGORY_UNSCHEDULED,
                                answerList = answerList,
                                duration = durationList
                            ), getLat(), getLong())
                    }
                }
                Params.TYPE_NEONBOX -> {
                    presenter.inprogressRepository.unscheduledNb?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnschAuto(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                    }
                }
                Params.TYPE_CLEAN -> {
                    presenter.inprogressRepository.unscheduledCl?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnschAuto(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList,
                            suhuAnswer = data.suhu_answer?:"",
                            suhuPicture = data.suhu_picture?:""
                        ), getLat(), getLong())
                    }
                }
                Params.TYPE_MCDS -> {
                    presenter.inprogressRepository.unscheduledMcds?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnschAuto(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                    }
                }
                Params.TYPE_QC -> {
                    presenter.inprogressRepository.unscheduledQc?.find { it.id == schedule.id!! }?.let { data ->
                        val answerList: MutableList<SaveAnswerBody.Answer> = mutableListOf()
                        val durationList: MutableList<SaveAnswerBody.Duration> = mutableListOf()
                        for (child in data.itemList ?: listOf()) {
                            for (form in child.formList ?: listOf()) {
                                for (answer in form.questionList ?: listOf()) {
                                    answerList += SaveAnswerBody.Answer(
                                        child.id!!,
                                        answer.id!!,
                                        answer.answerLocal?:""
                                    )
                                }
                            }
                        }

                        for (form in data.formList ?: listOf()) {
                            for (answer in form.questionList ?: listOf()) {
                                answerList += SaveAnswerBody.Answer(
                                    "",
                                    answer.id!!,
                                    answer.answerLocal?:""
                                )
                            }
                        }
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
                        presenter.saveAnswerUnschAuto(SaveAnswerBody(
                            orderId = data.id!!,
                            orderType = data.type!!,
                            category = Params.CATEGORY_UNSCHEDULED,
                            answerList = answerList,
                            duration = durationList
                        ), getLat(), getLong())
                    }
                }
            }
        }

    override fun onKunjunganPressed() {
        selectTab(TabList.Visit)
    }

    override fun onUnpredictPressed() {
        selectTab(TabList.Unscheduled)
    }

    override fun onTicketPressed() {
        selectTab(TabList.Ticket)
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(Observable.interval(5, TimeUnit.MINUTES).timeInterval().uiSubscribe({
            sendLocationTracking()
        }))
        addUiSubscription(Observable.interval(8, TimeUnit.MINUTES).timeInterval().uiSubscribe({
            if (isNetworkAvailable()) {
                proccessUnsent()
                proccessUnsentManual()
                proccessUnsentAuto()
            }
        }))
        addUiSubscription(RxBus.listen(GotoTicketEvent::class.java).subscribe {
            Handler().postDelayed({ selectTab(TabList.Ticket) }, 500)
        })
        addUiSubscription(RxBus.listen(UpdateStatusScheduledEvent::class.java).subscribe {
            if (isNetworkAvailable()) {
                proccessUnsent()
            }
        })
        addUiSubscription(RxBus.listen(UpdateStatusUnscheduleEvent::class.java).subscribe {
            if (isNetworkAvailable()) {
                proccessUnsentManual()
                proccessUnsentAuto()
            }
        })
    }

    private fun sendLocationTracking() {
        if (isNetworkAvailable()) {
            if (DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].rounded(5) == 0.0 &&
                DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].rounded(5) == 0.0) {
                if (locationPopup?.isShowing == false){
                    locationPopup?.show()
                }
            } else {
                myLoc()
                presenter.sendLocationTracking(
                    DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].rounded(5),
                    DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].rounded(5),
                    DeviceName.getDeviceName(),
                    DeviceInfo.getIMEI(this))
            }
        }
    }

    override fun onSentSchedule(dataList: List<Schedule>) {
        proccessUnsent()
        RxBus.publish(UpdateSEvent(dataList))
        RxBus.publish(UpdateStatusScheduledEvent())
    }

    override fun onSentScheduleManual(dataList: List<Unschedule>) {
        proccessUnsentManual()
        RxBus.publish(UpdateUMEvent(dataList))
        RxBus.publish(UpdateStatusUnscheduleEvent())
    }

    override fun onSentScheduleAuto(dataList: List<Unschedule>) {
        proccessUnsentAuto()
        RxBus.publish(UpdateUAEvent(dataList))
        RxBus.publish(UpdateStatusUnscheduleEvent())
    }

    override fun onResume() {
        super.onResume()
        presenter.checkVersion()
        //isMockAppInstalled()
    }
}