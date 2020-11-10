package com.akarinti.sapoe.view.main.visit.visit_parent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.event.GotoTicketEvent
import com.akarinti.sapoe.event.UpdateStatusScheduledEvent
import com.akarinti.sapoe.extension.getLat
import com.akarinti.sapoe.extension.getLong
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.extension.openMap
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.QuestionOffline
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.akarinti.sapoe.view.main.MainActivity
import com.akarinti.sapoe.view.main.visit.suhu.SuhuActivity
import com.akarinti.sapoe.view.main.visit.visit_parent.adapter.ChildAcOfflineAdapter
import com.akarinti.sapoe.view.main.visit.visit_parent.adapter.ChildCleanOfflineAdapter
import com.akarinti.sapoe.view.main.visit.visit_parent.adapter.ChildNbOfflineAdapter
import com.akarinti.sapoe.view.main.visit.visit_parent.adapter.QuestionOfflineAdapter
import com.akarinti.sapoe.view.main.visit.visit_parent.child.VisitChildActivity
import com.akarinti.sapoe.view.main.visit.visit_parent.question.SQuestionActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_visit_parent.*
import kotlinx.android.synthetic.main.button_navigation.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class VisitParentActivity : BaseMvpActivity<VisitParentPresenter>(), VisitParentContract.View,
    FinisDialogFragment.Listener, ErrorDialogFragment.Listener, ConfirmDialogFragment.Listener {

    companion object {
        fun newInstance(context: Context, schedule: Schedule) : Intent =
            context.intentFor<VisitParentActivity>(Params.BUNDLE_SCHEDULE to schedule)
    }

    private var schedule: Schedule? = null
    private lateinit var adapterCl: ChildCleanOfflineAdapter
    private lateinit var adapterNb: ChildNbOfflineAdapter
    private lateinit var adapterAc: ChildAcOfflineAdapter
    private lateinit var adapterQuestion: QuestionOfflineAdapter
    private var lat: Double? = null
    private var long: Double? = null
    private var curPos: Int = -1

    @Inject
    override lateinit var presenter: VisitParentPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        //if (isMockAppInstalled()) return

        initBundle()
        initView()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_visit_parent

    private fun initBundle() {
        intent?.extras?.let {
            schedule = it.getParcelable(Params.BUNDLE_SCHEDULE)
        }
        schedule?.let {
            showLoading()
            if (it.status == Params.STATUS_OPEN) {
                showLoading()
                presenter.updateStatusSchedule(it.id?:"", getLat(), getLong())
            } else {
                loadData()
            }
        }
    }

    private fun initView() {
        btnSimpan.text = getString(R.string.selesai)
        ivMap.visibility = View.VISIBLE
        tvTitle.setText(R.string.kunjungan_parent)
    }

    private fun initAction() {
        btnSimpan.setOnClickListener {
            ConfirmDialogFragment.newInstance(Params.TAG_TAKE, getString(R.string.save_order),
                getString(R.string.simpan), getString(R.string.batal), R.drawable.popup_warn)
                .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }

        ivMap.setOnClickListener {
            openMap(lat?:0.0, long?:0.0)
        }
    }

    private fun showFinisPopup() {
        FinisDialogFragment.newInstance(getString(R.string.sukses_simpan)
            , getString(R.string.kembali_beranda), R.drawable.popup_success
        ).show(supportFragmentManager, FinisDialogFragment::class.java.canonicalName)
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
        if (tag == Params.TAG_TAKE)
            saveOrder()
    }

    override fun onConfirmDialogDismissed() {
    }

    private fun saveOrder() {
        schedule?.let {
            when (it.type) {
                Params.TYPE_AC -> {
                    val orignData: MutableList<ParentAcOffline> = (presenter.inprogressRepository.scheduledAc ?: ArrayList()).toMutableList()
                    val dataAc = presenter.inprogressRepository.scheduledAc?.find { it.id == schedule?.id!! }
                    if (null != dataAc) {
                        orignData.remove(dataAc)
                        dataAc.end = Calendar.getInstance().timeInMillis / 1000L
                        orignData.add(dataAc)
                    }
                    presenter.inprogressRepository.scheduledAc = orignData

                    dataAc?.let { data ->
                        showLoading()
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
                        ), isNetworkAvailable(), getLat(), getLong())
                    }
                }
                Params.TYPE_NEONBOX -> {
                    val orignData: MutableList<ParentNbOffline> = (presenter.inprogressRepository.scheduledNb ?: ArrayList()).toMutableList()
                    val dataNb = presenter.inprogressRepository.scheduledNb?.find { it.id == schedule?.id!! }
                    if (null != dataNb) {
                        orignData.remove(dataNb)
                        dataNb.end = Calendar.getInstance().timeInMillis / 1000L
                        orignData.add(dataNb)
                    }
                    presenter.inprogressRepository.scheduledNb = orignData

                    dataNb?.let { data ->
                        showLoading()
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
                        ), isNetworkAvailable(), getLat(), getLong())
                    }
                }
                Params.TYPE_CLEAN -> {
                    val orignData: MutableList<ParentCleanOffline> = (presenter.inprogressRepository.scheduledCl ?: ArrayList()).toMutableList()
                    val dataCl = presenter.inprogressRepository.scheduledCl?.find { it.id == schedule?.id!! }
                    if (null != dataCl) {
                        orignData.remove(dataCl)
                        dataCl.end = Calendar.getInstance().timeInMillis / 1000L
                        orignData.add(dataCl)
                    }
                    presenter.inprogressRepository.scheduledCl = orignData

                    dataCl?.let { data ->
                        showLoading()
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
                        durationList += SaveAnswerBody.Duration(
                            "",
                            data.start,
                            data.end
                        )
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
                        ), isNetworkAvailable(), getLat(), getLong())
                    }
                }
                else -> {}
            }
        }
    }

    override fun onFinishDialogBtnPressed() {
        startActivity(intentFor<MainActivity>())
        finishAffinity()
    }

    private fun initQuestionList(data: List<QuestionOffline>?) {
        adapterQuestion = QuestionOfflineAdapter(data?: listOf())
        rvQuestion.apply {
            layoutManager = LinearLayoutManager(this@VisitParentActivity)
            adapter = adapterQuestion
            isNestedScrollingEnabled = false
        }
        adapterQuestion.setOnItemClickListener { _, _, position ->
            curPos = position
            SQuestionActivity.newInstance(this, schedule?.id, schedule?.type, position)
        }
    }

    override fun onQuestionaireParentAc() {
        presenter.inprogressRepository.scheduledAc?.find { it.id == schedule?.id!! }?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_ac)
            adapterAc = ChildAcOfflineAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@VisitParentActivity)
                adapter = adapterAc
                isNestedScrollingEnabled = false
            }
            adapterAc.setOnItemClickListener { _, _, pos ->
                VisitChildActivity.newInstance(this, schedule?.id, schedule?.type, pos)
            }
            initQuestionList(it.formList)
            btnSimpan.isEnabled = (it.itemList?.all { child -> child.isDone == true } == true ) && (it.formList?.all { question -> question.isDone == true } == true)
        }
        suhuContainer.visibility=View.GONE
        dismissLoading()
    }

    override fun onQuestionaireParentNb() {
        presenter.inprogressRepository.scheduledNb?.find { it.id == schedule?.id!! }?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_nb)
            adapterNb = ChildNbOfflineAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@VisitParentActivity)
                adapter = adapterNb
                isNestedScrollingEnabled = false
            }
            adapterNb.setOnItemClickListener { _, _, pos ->
                VisitChildActivity.newInstance(this, schedule?.id, schedule?.type, pos)
            }
            initQuestionList(it.formList)
            btnSimpan.isEnabled = (it.itemList?.all { child -> child.isDone == true } == true ) && (it.formList?.all { question -> question.isDone == true } == true)
        }
        suhuContainer.visibility=View.GONE
        dismissLoading()
    }

    override fun onQuestionaireParentClean() {
        presenter.inprogressRepository.scheduledCl?.find { it.id == schedule?.id!! }?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_clean)
            if(it.suhu_picture.isNullOrEmpty())
            ivSuhuStatus.setImageResource(R.drawable.ic_isi_form)
            else
            ivSuhuStatus.setImageResource(R.drawable.ic_complete)
            adapterCl = ChildCleanOfflineAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@VisitParentActivity)
                adapter = adapterCl
                isNestedScrollingEnabled = false
            }
            adapterCl.setOnItemClickListener { _, _, pos ->
                VisitChildActivity.newInstance(this, schedule?.id, schedule?.type, pos)
            }
            suhuContainer.setOnClickListener {
                SuhuActivity.newInstance(this, schedule?.id, true)
            }
            ivSuhuStatus.setImageResource(if (it.suhu_answer.isNullOrEmpty()) R.drawable.ic_isi_form else R.drawable.ic_complete)
            initQuestionList(it.formList)
            btnSimpan.isEnabled = (it.itemList?.all { child -> child.isDone == true } == true ) && (it.formList?.all { question -> question.isDone == true } == true)&& !it.suhu_answer.isNullOrEmpty()
        }
        suhuContainer.visibility=View.VISIBLE
        dismissLoading()
    }

    override fun onUpdateStatus() {
        dismissLoading()
        RxBus.publish(UpdateStatusScheduledEvent())
        loadData()
    }

    override fun onAnswerSaved() {
        dismissLoading()
        RxBus.publish(UpdateStatusScheduledEvent())
        loadData()
        showFinisPopup()
    }

    private fun loadData() {
        showLoading()
        schedule?.let {
            lat = it.location?.lat
            long = it.location?.long
            when (it.type) {
                Params.TYPE_AC -> {
                    if ( null != presenter.inprogressRepository.scheduledAc?.find { obj -> obj.id == it.id })
                        onQuestionaireParentAc()
                    else
                        presenter.getQuestionaireParentAC(it.id?:"", getLat(), getLong())
                }
                Params.TYPE_NEONBOX -> {
                    if ( null != presenter.inprogressRepository.scheduledNb?.find { obj -> obj.id == it.id })
                        onQuestionaireParentNb()
                    else
                        presenter.getQuestionaireParentNB(it.id?:"", getLat(), getLong())
                }
                Params.TYPE_CLEAN -> {
                    if ( null != presenter.inprogressRepository.scheduledCl?.find { obj -> obj.id == it.id })
                        onQuestionaireParentClean()
                    else
                        presenter.getQuestionaireParentClean(it.id?:"", getLat(), getLong())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CodeIntent.OPEN_QUESTION && resultCode == Activity.RESULT_OK) {
            schedule?.let {
                when (it.type) {
                    Params.TYPE_AC -> onQuestionaireParentAc()
                    Params.TYPE_NEONBOX -> onQuestionaireParentNb()
                    Params.TYPE_CLEAN -> onQuestionaireParentClean()
                }
            }
            if (curPos != -1 && adapterQuestion.data.size > curPos+1) {
                for (i in curPos until adapterQuestion.data.size) {
                    if (adapterQuestion.data[i].isDone != true) {
                        curPos = i
                        SQuestionActivity.newInstance(this, schedule?.id, schedule?.type, curPos)
                        break
                    }
                }
            }
        }
    }

    override fun errorScreen(message: String?, code: Int?) {
        if (code == 400) {
            ErrorDialogFragment.newInstance(
                Params.TAG_TAKE, message?:"-", getString(R.string.ok_mengerti)
            ).show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
        } else {
            super.errorScreen(message, code)
        }
    }

    override fun onErrorDialogBtnPressed(tag: String?) {
        finish()
    }

    override fun onErrorDialogDismissed(tag: String?) {
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(GotoTicketEvent::class.java).subscribe {
            finish()
        })
    }
}